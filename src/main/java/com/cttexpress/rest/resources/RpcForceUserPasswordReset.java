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
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminResetUserPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminResetUserPasswordResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserNotFoundException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.NotAuthorizedException;


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

@Path("/rpc-force-user-password-reset")
@Produces(MediaType.APPLICATION_JSON)
public class RpcForceUserPasswordReset {


    private static final Logger LOGGER = LoggerFactory.getLogger(RpcForceUserPasswordReset.class);
    private final Validator validator;
    @Context
    ServletContext context;

    public RpcForceUserPasswordReset(Validator validator) {
        this.validator = validator;
    }

    @POST
    @RolesAllowed({"ADMIN"})
    @Path("/{userEmail}")
    public Response rpcForceUserPasswordReset(
            @Context HttpServletRequest req,
            @Context UriInfo uriInfo,
            @Length(min=10, max=128) @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$") @PathParam("userEmail") String userEmail) throws URISyntaxException, CustomException {

        LOGGER.info("POST rpcForceUserPasswordReset [" + userEmail + "] Remote Host[" +
                req.getRemoteHost() +
                "] Remote Address[" + req.getRemoteAddr() +
                "] Remote Port[" + String.valueOf(req.getRemotePort()) + "].");
        try {

            adminResetUserPassword(userEmail);
            return Response
                    .status(Status.NO_CONTENT)
                    .build();

        } catch (UserNotFoundException ex) {
            throw new CustomException(
                    Status.NOT_FOUND,
                    "USER-NOT-FOUND",
                    "El usuario [" + userEmail + "] no existe en el user Pool.",
                    ex);
        } catch (NotAuthorizedException ex) {
            if (ex.getMessage() != null && ex.getMessage().indexOf("User is disabled") >= 0) {
                throw new CustomException(
                        Status.CONFLICT,
                        "USER-IS-DISABLED",
                        "El usuario [" + userEmail + "] está DESHABILITADO.",
                        ex);
            } else {
                throw new CustomException(
                        Status.INTERNAL_SERVER_ERROR,
                        "ERR-500-RPC-FORCE-USER-PASSWORD_RESET",
                        "process-exception-rpc-force-user-password-reset",
                        ex);
            }
        } catch (Throwable ex) {
            throw new CustomException(
                    Status.INTERNAL_SERVER_ERROR,
                    "ERR-500-RPC-FORCE-USER-PASSWORD_RESET",
                    "process-exception-rpc-force-user-password-reset",
                    ex);
        }
    }


    protected static AdminResetUserPasswordResponse adminResetUserPassword(String userEmail) {

        AdminResetUserPasswordResponse adminResetUserPasswordResponse = null;

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                MyAwsBasicCredentials.ACCESS_KEY_ID,
                MyAwsBasicCredentials.SECRET_ACCESS_KEY);

        try ( CognitoIdentityProviderClient cognitoclient = CognitoIdentityProviderClient.builder()
                .region(MyAwsUserPool.REGION)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build()){
                adminResetUserPasswordResponse = cognitoclient.adminResetUserPassword(
                    AdminResetUserPasswordRequest.builder()
                            .userPoolId(MyAwsUserPool.USER_POOL_ID)
                            .username(userEmail)
                            .build());
        } catch (Throwable e) {
            throw e;
        }
        return adminResetUserPasswordResponse;
    }
}
