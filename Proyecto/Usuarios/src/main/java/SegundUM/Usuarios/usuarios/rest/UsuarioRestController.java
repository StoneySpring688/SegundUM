package SegundUM.Usuarios.usuarios.rest;

import java.net.URI;
import java.security.Principal;
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
import javax.ws.rs.core.SecurityContext;
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

/** Controlador JAX-RS que expone la API REST de usuarios bajo /api/usuarios; delega la lógica en ServicioUsuarios. */
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

    @GET
    @RolesAllowed("USUARIO")
    public Response getAllusuarios() throws Exception {
        logger.info("Petición recibida: GET /usuarios/");

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

    
    /**
     * Recuperar el usuario logeado
     **/
    @GET
    @Path("/logged")
    @RolesAllowed("USUARIO")
    public Response getUsuario(@Context SecurityContext securityContext) throws Exception {
    	logger.info("obteniendo id de usuario de la cookie");
    	Claims claims = (Claims) requestContext.getProperty("claims");
        String id = claims.getSubject();
        logger.info("Petición recibida: GET /usuarios/logged}", id);

        Usuario usuario = servicioUsuarios.getUserById(id);

        UsuarioDTO dto = UsuarioDTO.fromEntity(usuario);
        String url = urlUsuario(usuario.getId()).toString();
        dto.addLink("self", url);
        dto.addLink("all", UriBuilder.fromUri(uriInfo.getBaseUri()).path("usuarios").build().toString());
        dto.addLink("modificar", url);
        dto.addLink("eliminar", url);

        logger.info("Usuario {} recuperado.", id);
        return Response.ok(dto).build();
    }

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

    @PUT
    @RolesAllowed("USUARIO")
    public Response modificarUsuario(
            NuevoUsuarioDTO dto) throws Exception {

        if (dto == null) {
            throw new IllegalArgumentException("El cuerpo de la petición es obligatorio");
        }

        Claims claims = (Claims) requestContext.getProperty("claims");
        String idUsuarioAutenticado = claims.getSubject();

        servicioUsuarios.modificarUsuario(idUsuarioAutenticado, dto.nombre, dto.apellidos,
                dto.clave, dto.fechaNacimiento, dto.telefono);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("USUARIO")
    public Response eliminarUsuario(@PathParam("id") String id) throws Exception {
        servicioUsuarios.deleteUserById(id);
        return Response.noContent().build();
    }

    /** Devuelve solo el ID del usuario por email; usado internamente por otros microservicios. */
    @GET
    @Path("/email/{email}")
    @PermitAll
    public Response obtenerIdPorEmail(@PathParam("email") String email) throws Exception {
        logger.info("Búsqueda de ID de usuario por email: {}", email);
        Usuario usuario = servicioUsuarios.getUserByEmail(email);
        return Response.ok("{\"id\":\"" + usuario.getId() + "\"}").type(MediaType.APPLICATION_JSON).build();
    }

    /** Verifica credenciales email/clave; llamado internamente por la pasarela para el flujo de login. */
    @GET
    @Path("/sesion/{email}/{clave}")
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

    /** Recupera el usuario asociado a un ID de GitHub; usado por la pasarela tras el OAuth2 callback. */
    @GET
    @Path("/github/{idGitHub}")
    @PermitAll
    public Response verificarGitHub(@PathParam("idGitHub") String idGitHub) throws Exception {

        logger.info("Recuperación de usuario por GitHub ID: {}", idGitHub);
        Usuario usuario = servicioUsuarios.getUsuarioPorIdGitHub(idGitHub);

        UsuarioDTO dto = UsuarioDTO.fromEntity(usuario);
        dto.addLink("self", urlUsuario(usuario.getId()).toString());
        return Response.ok(dto).build();
    }
}
