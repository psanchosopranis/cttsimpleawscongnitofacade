package com.cttexpress.rest.resources;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.cttexpress.rest.exceptions.CustomException;
import com.cttexpress.rest.representations.AppStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Validator;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class AppStatusResource {


    private static final Logger LOGGER = LoggerFactory.getLogger(AppStatusResource.class);
    private final Validator validator;
    @Context
    ServletContext context;

    public AppStatusResource(Validator validator) {
        this.validator = validator;
    }

    @GET
    public Response getCurrentAppStatus(@Context HttpServletRequest req) throws CustomException {

        LOGGER.info("GET getCurrentAppStatus Remote Host[" +
                req.getRemoteHost() +
                "] Remote Address[" + req.getRemoteAddr() +
                "] Remote Port[" + String.valueOf(req.getRemotePort()) + "].");

        try {
            AppStatus appStatus = new AppStatus("OK");
            return Response
                    .status(Response.Status.OK)
                    .entity(appStatus).build();

        } catch (Throwable ex) {
            throw new CustomException(
                    Status.INTERNAL_SERVER_ERROR,
                    "ERR-500-APPSTATUS-RETRIEVAL",
                    "process-exception-appstatus-retrieval",
                    ex);
        }
    }

}
