package com.cttexpress.rest.resources;

import com.cttexpress.config.MyAwsBasicCredentials;
import com.cttexpress.config.MyAwsUserPool;
import com.cttexpress.rest.exceptions.CustomException;
import com.cttexpress.rest.representations.CreateUserRequest;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserNotFoundException;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.net.URISyntaxException;
import java.util.HashMap;

@Path("/rpc-create-user")
@Produces(MediaType.APPLICATION_JSON)
public class RpcCreateUser {


    private static final Logger LOGGER = LoggerFactory.getLogger(RpcCreateUser.class);
    private final Validator validator;
    @Context
    ServletContext context;

    public RpcCreateUser(Validator validator) {
        this.validator = validator;
    }

    @POST
    //@RolesAllowed({"ADMIN"})
    public Response rpcCreateUser(
            @Context HttpServletRequest req,
            @Context UriInfo uriInfo,
            CreateUserRequest createUserRequest) throws URISyntaxException, CustomException {

        LOGGER.info("POST rpcCreateUser Remote Host[" +
                req.getRemoteHost() +
                "] Remote Address[" + req.getRemoteAddr() +
                "] Remote Port[" + String.valueOf(req.getRemotePort()) + "].");
        try {


            return Response
                    .status(Status.OK).entity(createUserRequest)
                    .build();

        } catch (Throwable ex) {
            throw new CustomException(
                    Status.INTERNAL_SERVER_ERROR,
                    "ERR-500-RPC-CREATE-USER",
                    "process-exception-rpc-create-user",
                    ex);
        }
    }



}

