package SegundUM.pasarela.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/** Filtro por petición que valida el JWT (de cabecera o cookie) y carga la autenticación en el contexto de seguridad. */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    private final JwtUtils jwtUtils;

    @Autowired
    public JwtRequestFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String jwt = jwtUtils.extractToken(request);

        if (jwt != null) {
            String username = null;
            try {
                username = jwtUtils.extractUsername(jwt);
            } catch (Exception e) {
                logger.warn("Token JWT inválido: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token JWT inválido");
                return;
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtils.validateToken(jwt)) {
                    // Extraer claims para convertir los roles en autoridades de Spring Security
                    Claims claims = Jwts.parser()
                            .setSigningKey(JwtUtils.SECRET_KEY.getBytes())
                            .parseClaimsJws(jwt)
                            .getBody();

                    @SuppressWarnings("unchecked")
                    List<String> roles = claims.get("roles", List.class);

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            username, null, roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    logger.warn("Token JWT no válido para el usuario: {}", username);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token JWT no válido");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }
}
