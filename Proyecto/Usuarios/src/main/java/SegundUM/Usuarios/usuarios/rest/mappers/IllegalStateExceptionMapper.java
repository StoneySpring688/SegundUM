package SegundUM.Usuarios.usuarios.rest.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Traduce IllegalStateException a una respuesta HTTP 409 con cuerpo JSON. */
@Provider
public class IllegalStateExceptionMapper implements ExceptionMapper<IllegalStateException> {

    private static final Logger logger = LoggerFactory.getLogger(IllegalStateExceptionMapper.class);

    @Override
    public Response toResponse(IllegalStateException exception) {
        logger.warn("Conflicto de estado: {}", exception.getMessage());
        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(409, exception.getMessage()))
                .build();
    }
}
