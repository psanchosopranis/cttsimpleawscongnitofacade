package com.cttexpress.rest.auth;

import com.cttexpress.rest.representations.ErrorResponse;
import io.dropwizard.auth.UnauthorizedHandler;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomUnauthorizedHandler implements UnauthorizedHandler {

    private static final String CHALLENGE_FORMAT = "%s realm=\"%s\"";

    @Override
    public Response buildResponse(String prefix, String realm) {

        ErrorResponse errorResponse = new ErrorResponse(
                "ERR-401-UNAUTHORIZED",
                "invalid-or-missing-credentials",
                new ArrayList<String>(Arrays.asList("Se requieren credenciales válidas (y activas) para acceder a este recurso."))
        );

        return Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE,
                        String.format(CHALLENGE_FORMAT, "Basic", "cttexpresssimpleiam"))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(errorResponse)
                .build();
    }

}
