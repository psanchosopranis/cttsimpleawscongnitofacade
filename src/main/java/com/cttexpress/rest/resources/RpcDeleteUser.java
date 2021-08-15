package com.cttexpress.rest.resources;

import com.cttexpress.config.MyAwsBasicCredentials;
import com.cttexpress.config.MyAwsUserPool;
import com.cttexpress.rest.exceptions.CustomException;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDeleteUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDeleteUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserNotFoundException;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import javax.validation.constraints.Pattern;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.net.URISyntaxException;

@Path("/rpc-delete-user")
@Produces(MediaType.APPLICATION_JSON)
public class RpcDeleteUser {


    private static final Logger LOGGER = LoggerFactory.getLogger(RpcDeleteUser.class);
    private final Validator validator;
    @Context
    ServletContext context;

    public RpcDeleteUser(Validator validator) {
        this.validator = validator;
    }

    @POST
    @RolesAllowed({"ADMIN"})
    @Path("/{userEmail}")
    public Response rpcDeleteUser(
            @Context HttpServletRequest req,
            @Context UriInfo uriInfo,
            @Length(min=10, max=128) @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$") @PathParam("userEmail") String userEmail) throws URISyntaxException, CustomException {

        LOGGER.info("POST rpcDeleteUser [" + userEmail + "] Remote Host[" +
                req.getRemoteHost() +
                "] Remote Address[" + req.getRemoteAddr() +
                "] Remote Port[" + String.valueOf(req.getRemotePort()) + "].");
        try {

            adminDeleteUser(userEmail);
            return Response
                    .status(Status.NO_CONTENT)
                    .build();

        } catch (UserNotFoundException ex) {
            throw new CustomException(
                    Status.NOT_FOUND,
                    "USER-NOT-FOUND",
                    "El usuario ["+ userEmail + "] no existe en el user Pool.",
                    ex);
        } catch (Throwable ex) {
            throw new CustomException(
                    Status.INTERNAL_SERVER_ERROR,
                    "ERR-500-RPC-DELETE-USER",
                    "process-exception-rpc-delete-user",
                    ex);
        }
    }


    protected static AdminDeleteUserResponse adminDeleteUser(String userEmail) {

        AdminDeleteUserResponse AdminDeleteUserResponse = null;

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                MyAwsBasicCredentials.ACCESS_KEY_ID,
                MyAwsBasicCredentials.SECRET_ACCESS_KEY);

        try ( CognitoIdentityProviderClient cognitoclient = CognitoIdentityProviderClient.builder()
                .region(MyAwsUserPool.REGION)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build()){
            AdminDeleteUserResponse = cognitoclient.adminDeleteUser(
                    AdminDeleteUserRequest.builder()
                            .userPoolId(MyAwsUserPool.USER_POOL_ID)
                            .username(userEmail)
                            .build());
        } catch (Throwable e) {
            throw e;
        }
        return AdminDeleteUserResponse;
    }
}

