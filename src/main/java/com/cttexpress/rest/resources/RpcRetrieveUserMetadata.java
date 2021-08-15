package com.cttexpress.rest.resources;

import com.cttexpress.config.MyAwsBasicCredentials;
import com.cttexpress.config.MyAwsUserPool;
import com.cttexpress.rest.exceptions.CustomException;
import com.cttexpress.rest.representations.RetrieveUserMetadataResponse;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Path("/rpc-retrieve-user-metadata")
@Produces(MediaType.APPLICATION_JSON)
public class RpcRetrieveUserMetadata {


    private static final Logger LOGGER = LoggerFactory.getLogger(RpcRetrieveUserMetadata.class);
    private final Validator validator;
    @Context
    ServletContext context;

    public RpcRetrieveUserMetadata(Validator validator) {
        this.validator = validator;
    }

    @POST
    @RolesAllowed({"ADMIN"})
    @Path("/{userEmail}")
    public Response rpcGetUser(
            @Context HttpServletRequest req,
            @Context UriInfo uriInfo,
            @Length(min=10, max=128) @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$") @PathParam("userEmail") String userEmail) throws URISyntaxException, CustomException {

        LOGGER.info("POST rpcGetUser [" + userEmail + "] Remote Host[" +
                req.getRemoteHost() +
                "] Remote Address[" + req.getRemoteAddr() +
                "] Remote Port[" + String.valueOf(req.getRemotePort()) + "].");
        try {

            AdminGetUserResponse adminGetUserResponse = adminGetUser(userEmail);
            RetrieveUserMetadataResponse retrieveUserMetadataResponse = new RetrieveUserMetadataResponse();

            HashMap<String, String> theUserAttributesMap = retrieveUserMetadataResponse.getUser_attributes();
            List<AttributeType> attributeTypeList = adminGetUserResponse.userAttributes();
            for ( AttributeType attributeType : attributeTypeList ) {
                theUserAttributesMap.put(attributeType.name(), attributeType.value());
            }
            retrieveUserMetadataResponse.setUser_id(adminGetUserResponse.username());
            retrieveUserMetadataResponse.setUser_status(adminGetUserResponse.userStatusAsString());
            retrieveUserMetadataResponse.setUser_is_enabled(adminGetUserResponse.enabled());
            retrieveUserMetadataResponse.setUser_create_date(Date.from(adminGetUserResponse.userCreateDate()).toString());
            retrieveUserMetadataResponse.setUser_last_modified_date(Date.from(adminGetUserResponse.userLastModifiedDate()).toString());

            return Response
                    .status(Status.OK)
                    .entity(retrieveUserMetadataResponse)
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
                    "ERR-500-RPC-RETRIEVE-USER-METADATA",
                    "process-exception-rpc-retrieve-user-metadata",
                    ex);
        }
    }


    protected static AdminGetUserResponse adminGetUser(String userEmail) {

        AdminGetUserResponse AdminGetUserResponse = null;

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                MyAwsBasicCredentials.ACCESS_KEY_ID,
                MyAwsBasicCredentials.SECRET_ACCESS_KEY);

        try ( CognitoIdentityProviderClient cognitoclient = CognitoIdentityProviderClient.builder()
                .region(MyAwsUserPool.REGION)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build()){
            AdminGetUserResponse = cognitoclient.adminGetUser(
                    AdminGetUserRequest.builder()
                            .userPoolId(MyAwsUserPool.USER_POOL_ID)
                            .username(userEmail)
                            .build());
        } catch (Throwable e) {
            throw e;
        }
        return AdminGetUserResponse;
    }
}
