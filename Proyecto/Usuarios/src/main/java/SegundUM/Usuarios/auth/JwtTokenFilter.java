package SegundUM.Usuarios.auth;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Priority;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.ws.rs.core.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** Filtro JAX-RS que valida el JWT (cabecera Authorization o cookie) y rechaza peticiones no autorizadas. */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtTokenFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    private static final String SECRET_KEY = "secreto_compartido_segundum_2026";

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Comprobamos si la ruta tiene la anotación @PermitAll
        if (resourceInfo.getResourceMethod()
                .isAnnotationPresent(PermitAll.class)) {
            return; // no se controla la autorización
        }

        String token = null;

        // 1. Intentar obtener de la cabecera Authorization
        String authorization = requestContext.getHeaderString("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring("Bearer ".length()).trim();
        } 
        // 2. Intentar obtener de la cookie 'jwt'
        else {
            Cookie cookie = requestContext.getCookies().get("jwt");
            if (cookie != null) {
                token = cookie.getValue();
            }
        }

        if (token == null) {
            logger.warn("Peticion sin token JWT para ruta protegida: {}", requestContext.getUriInfo().getPath());
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
        } else {
            try {
                // Validar el token
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY.getBytes())
                        .parseClaimsJws(token)
                        .getBody();

                // Poner los claims como propiedad de la petición para que estén disponibles en el controlador
                requestContext.setProperty("claims", claims);

                // Autorización basada en roles (soporta List<String> y String separado por comas)
                Set<String> roles = new HashSet<>();
                Object rolesObj = claims.get("roles");
                if (rolesObj instanceof java.util.List) {
                    @SuppressWarnings("unchecked")
                    java.util.List<String> rolesList = (java.util.List<String>) rolesObj;
                    roles.addAll(rolesList);
                } else if (rolesObj instanceof String) {
                    roles.addAll(Arrays.asList(((String) rolesObj).split(",")));
                }

                // Consulta si la operación está protegida por rol
                if (resourceInfo.getResourceMethod()
                        .isAnnotationPresent(RolesAllowed.class)) {

                    String[] allowedRoles = resourceInfo.getResourceMethod()
                            .getAnnotation(RolesAllowed.class).value();

                    if (roles.stream()
                            .noneMatch(userRole -> Arrays.asList(allowedRoles)
                                    .contains(userRole))) {
                        requestContext.abortWith(
                                Response.status(Response.Status.FORBIDDEN).build());
                    }
                }

            } catch (Exception e) {
                logger.warn("Token JWT invalido o caducado: {}", e.getMessage());
                requestContext.abortWith(
                        Response.status(Response.Status.UNAUTHORIZED).build());
            }
        }
    }
}
