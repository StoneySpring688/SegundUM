package SegundUM.Usuarios.usuarios.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import SegundUM.Usuarios.usuarios.modelo.ResumenUsuario;
import SegundUM.Usuarios.usuarios.modelo.Usuario;
import SegundUM.Usuarios.usuarios.rest.dto.NuevoUsuarioDTO;
import SegundUM.Usuarios.usuarios.rest.dto.UsuarioDTO;
import SegundUM.Usuarios.servicio.FactoriaServicios;
import SegundUM.Usuarios.usuarios.servicio.ServicioUsuarios;

/**
 * Controlador REST para la gestión de usuarios.
 *
 * Las excepciones se propagan al contenedor JAX-RS y son traducidas
 * a códigos HTTP por los {@code ExceptionMapper} registrados.
 *
 * Base path: /api/usuarios
 */
@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioRestController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioRestController.class);

    @Context
    private UriInfo uriInfo;

    @Context
    private ContainerRequestContext requestContext;

    private final ServicioUsuarios servicioUsuarios;

    public UsuarioRestController() {
        this.servicioUsuarios = FactoriaServicios.getServicio(ServicioUsuarios.class);
    }

    private URI urlUsuario(String id) {
        return uriInfo.getBaseUriBuilder().path("usuarios").path(id).build();
    }

    /** GET /usuarios/getAll — Listado de usuarios con HATEOAS */
    @GET
    @RolesAllowed("USUARIO")
    public Response getAllusuarios() throws Exception {
        logger.info("Petición recibida: GET /usuarios/getAll");

        List<Usuario> usuarios = servicioUsuarios.getAllUsuarios();

        if (usuarios.isEmpty()) {
            logger.info("No se encontraron usuarios registrados.");
            return Response.noContent().build();
        }

        List<Listado.ResumenExtendido> items = new ArrayList<>();
        for (Usuario u : usuarios) {
            ResumenUsuario resumen = new ResumenUsuario(u.getId(), u.getEmail(), u.getNombre(),
                    u.getApellidos(), u.getFechaNacimiento(), u.getTelefono(), u.isAdministrador(),
                    u.getVentasRealizadas(), u.getComprasRealizadas());
            Listado.ResumenExtendido ext = new Listado.ResumenExtendido();
            ext.setResumen(resumen);
            ext.setUrl(urlUsuario(u.getId()).toString());
            items.add(ext);
        }

        Listado listado = new Listado();
        listado.setResumenEntidad(items);

        logger.info("Retornando listado con {} usuarios.", items.size());
        return Response.ok(listado).build();
    }

    /** GET /usuarios/{id} — Recuperación de usuario con HATEOAS */
    @GET
    @Path("/{id}")
    @RolesAllowed("USUARIO")
    public Response getUsuario(@PathParam("id") String id) throws Exception {
        logger.info("Petición recibida: GET /usuarios/{}", id);

        Usuario usuario = servicioUsuarios.getUserById(id);

        UsuarioDTO dto = UsuarioDTO.fromEntity(usuario);
        dto.addLink("self", urlUsuario(usuario.getId()).toString());
        dto.addLink("all", UriBuilder.fromUri(uriInfo.getBaseUri())
                .path("usuarios").path("getAll").build().toString());

        logger.info("Usuario {} recuperado.", id);
        return Response.ok(dto).build();
    }

    /** POST /usuarios — Alta de usuario (datos en el cuerpo) */
    @POST
    @PermitAll
    public Response registrarUsuario(NuevoUsuarioDTO dto) throws Exception {
        if (dto == null) {
            throw new IllegalArgumentException("El cuerpo de la petición es obligatorio");
        }
        String id = servicioUsuarios.altaUsuario(dto.email, dto.nombre, dto.apellidos,
                dto.clave, dto.fechaNacimiento, dto.telefono);

        URI nuevaURI = urlUsuario(id);
        return Response.created(nuevaURI).entity(id).build();
    }

    /** POST /usuarios/github — Alta de usuario vía GitHub (datos en el cuerpo) */
    @POST
    @Path("/github")
    @PermitAll
    public Response registrarUsuarioGitHub(NuevoUsuarioDTO dto) throws Exception {
        if (dto == null) {
            throw new IllegalArgumentException("El cuerpo de la petición es obligatorio");
        }
        logger.info("Solicitud de registro vía GitHub: {}", dto.idGitHub);
        String id = servicioUsuarios.altaUsuarioGitHub(dto.idGitHub, dto.nombre, dto.email);
        Usuario usuario = servicioUsuarios.getUserById(id);

        UsuarioDTO out = UsuarioDTO.fromEntity(usuario);
        out.addLink("self", urlUsuario(id).toString());

        return Response.created(urlUsuario(id)).entity(out).build();
    }

    /** PUT /usuarios/{id} — Modificar varios campos (datos en el cuerpo) */
    @PUT
    @Path("/{id}")
    @RolesAllowed("USUARIO")
    public Response modificarUsuario(
            @PathParam("id") String usuarioId,
            NuevoUsuarioDTO dto) throws Exception {

        if (dto == null) {
            throw new IllegalArgumentException("El cuerpo de la petición es obligatorio");
        }

        Claims claims = (Claims) requestContext.getProperty("claims");
        String idUsuarioAutenticado = claims.getSubject();

        if (!usuarioId.equals(idUsuarioAutenticado)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("No tienes permiso para modificar este usuario.").build();
        }

        servicioUsuarios.modificarUsuario(usuarioId, dto.nombre, dto.apellidos,
                dto.clave, dto.fechaNacimiento, dto.telefono);
        return Response.noContent().build();
    }

    /** DELETE /usuarios/{id} — Eliminar usuario */
    @DELETE
    @Path("/{id}")
    @RolesAllowed("USUARIO")
    public Response eliminarUsuario(@PathParam("id") String id) throws Exception {
        servicioUsuarios.deleteUserById(id);
        return Response.noContent().build();
    }

    /** GET /usuarios/verificar/{email}/{clave} — Verificar credenciales (uso interno de la pasarela) */
    @GET
    @Path("/verificar/{email}/{clave}")
    @PermitAll
    public Response verificarCredenciales(
            @PathParam("email") String email,
            @PathParam("clave") String clave) throws Exception {

        logger.info("Verificación de credenciales para: {}", email);
        Usuario usuario = servicioUsuarios.login(email, clave);

        UsuarioDTO dto = UsuarioDTO.fromEntity(usuario);
        dto.addLink("self", urlUsuario(usuario.getId()).toString());
        return Response.ok(dto).build();
    }

    /** GET /usuarios/verificar-github/{idGitHub} — Recuperar usuario por ID de GitHub */
    @GET
    @Path("/verificar-github/{idGitHub}")
    @PermitAll
    public Response verificarGitHub(@PathParam("idGitHub") String idGitHub) throws Exception {

        logger.info("Recuperación de usuario por GitHub ID: {}", idGitHub);
        Usuario usuario = servicioUsuarios.getUsuarioPorIdGitHub(idGitHub);

        UsuarioDTO dto = UsuarioDTO.fromEntity(usuario);
        dto.addLink("self", urlUsuario(usuario.getId()).toString());
        return Response.ok(dto).build();
    }
}
