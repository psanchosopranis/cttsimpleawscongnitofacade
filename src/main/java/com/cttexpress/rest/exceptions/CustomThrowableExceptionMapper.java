package com.cttexpress.rest.exceptions;

import com.cttexpress.rest.representations.ErrorResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;

@Provider
public class CustomThrowableExceptionMapper implements ExceptionMapper<Throwable> {
    public Response toResponse(Throwable exception) {

        ArrayList<String> errorMessages = new ArrayList<String>();
        String mensaje = exception.getClass().getName() + "[ " + (exception.getMessage() != null ? exception.getMessage() : "(mensaje de error no disponible)") + "]";
        errorMessages.add("mensaje:" + mensaje);

        Throwable cause = exception.getCause();
        if (cause != null) {
            mensaje = cause.getClass().getName() + "[ " + (cause.getMessage() != null ? cause.getMessage() : "(mensaje de error no disponible)") + "]";
            errorMessages.add("causa interna:" + mensaje);
        }

        ErrorResponse errorResponse = null;
        Response.Status responseStatus = null;
        if (exception instanceof javax.ws.rs.NotFoundException) {
            responseStatus = Response.Status.NOT_FOUND;
            errorResponse = new ErrorResponse(
                    "RESOURCE-NOT_FOUND",
                    "recurso-no-accesible",
                    errorMessages);
        } else if (exception instanceof javax.ws.rs.ForbiddenException) {
            responseStatus = Response.Status.FORBIDDEN;
            errorResponse = new ErrorResponse(
                    "FORBIDDEN-ACCESS",
                    "credenciales-no-validas-para-operacion",
                    errorMessages);
        } else if (exception instanceof javax.ws.rs.BadRequestException) {
            responseStatus = Response.Status.BAD_REQUEST;
            errorResponse = new ErrorResponse(
                    "BAD-REQUEST",
                    "error-validacion-datos-para-operacion",
                    errorMessages);
        } else if (exception instanceof javax.ws.rs.NotAllowedException) {
            responseStatus = Response.Status.METHOD_NOT_ALLOWED;
            errorResponse = new ErrorResponse(
                    "METHOD_NOT_ALLOWED",
                    "metodo-http-no-permitido-para-operacion",
                    errorMessages);
        } else if (exception instanceof javax.ws.rs.NotSupportedException) {
            responseStatus = Response.Status.UNSUPPORTED_MEDIA_TYPE;
            errorResponse = new ErrorResponse(
                    "UNSUPPORTED_MEDIA_TYPE",
                    "media-type-http-no-permitido-para-operacion",
                    errorMessages);
        } else {
            responseStatus = Response.Status.INTERNAL_SERVER_ERROR;
            errorResponse = new ErrorResponse(
                    "SERVER-THROWABLE-EXCEPTION",
                    "excepcion-general-procesamiento",
                    errorMessages);
        }

        return Response.status(responseStatus)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
