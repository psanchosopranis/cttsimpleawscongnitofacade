package com.cttexpress.rest.exceptions;

import com.cttexpress.rest.representations.ErrorResponse;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.Set;

@Provider
public class CustomConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    public Response toResponse(ConstraintViolationException exception) {

        ArrayList<String> errorMessages = new ArrayList<String>();
        String mensaje = exception.getClass().getName() + "[ " + (exception.getMessage() != null ? exception.getMessage() : "(mensaje de error no disponible)") + "]";
        errorMessages.add("excepcion:" + mensaje);

        Throwable cause = exception.getCause();
        if (cause != null) {
            mensaje = cause.getClass().getName() + "[ " + (cause.getMessage() != null ? cause.getMessage() : "(mensaje de error no disponible)") + "]";
            errorMessages.add("causa interna:" + mensaje);
        }

        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

        for ( ConstraintViolation<?> violation: constraintViolations ) {
            errorMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
        }

        ErrorResponse  errorResponse = new ErrorResponse(
                    "BAD-REQUEST",
                    "error-validacion-datos-para-operacion",
                    errorMessages);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
