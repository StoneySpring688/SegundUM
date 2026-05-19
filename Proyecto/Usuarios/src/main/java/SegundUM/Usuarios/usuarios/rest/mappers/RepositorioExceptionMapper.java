package SegundUM.Usuarios.usuarios.rest.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import SegundUM.Usuarios.repositorio.RepositorioException;

/** Traduce RepositorioException a una respuesta HTTP 500 con cuerpo JSON. */
@Provider
public class RepositorioExceptionMapper implements ExceptionMapper<RepositorioException> {

    private static final Logger logger = LoggerFactory.getLogger(RepositorioExceptionMapper.class);

    @Override
    public Response toResponse(RepositorioException exception) {
        Throwable causa = exception.getCause();
        String detalle = causa != null ? causa.getMessage() : exception.getMessage();
        logger.error("Error en repositorio: {}", detalle, exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(500, "Error en repositorio: " + detalle))
                .build();
    }
}
