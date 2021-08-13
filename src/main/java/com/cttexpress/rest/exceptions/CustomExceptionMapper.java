package com.cttexpress.rest.exceptions;

import com.cttexpress.rest.representations.ErrorResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<CustomException> {
    public Response toResponse(CustomException customException) {

        ArrayList<String> errorMessages = new ArrayList<String>();
        String mensaje = customException.getClass().getName() + "[ " + (customException.getMessage() != null ? customException.getMessage() : "(mensaje de error no disponible)") + "]";
        errorMessages.add("mensaje:" + mensaje);

        Throwable cause = customException.getCause();
        if (cause != null) {
            mensaje = cause.getClass().getName() + "[ " + (cause.getMessage() != null ? cause.getMessage() : "(mensaje de error no disponible)") + "]";
            errorMessages.add("causa interna:" + mensaje);
        }

        ErrorResponse errorResponse = new ErrorResponse(
                customException.getErrorId(),
                customException.errorAlias,
                errorMessages
        );

        return Response.status(customException.getCode())
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
