package com.cttexpress.rest.resources;

import com.cttexpress.config.MyAwsBasicCredentials;
import com.cttexpress.config.MyAwsUserPool;
import com.cttexpress.rest.exceptions.CustomException;
import com.cttexpress.rest.representations.ErrorResponse;
import com.cttexpress.rest.representations.UpdateUserCustomAttributesRequest;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

;

@Path("/rpc-update-user-custom-attributes")
@Produces(MediaType.APPLICATION_JSON)
public class RpcUpdateUserCustomAttributes {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcUpdateUserCustomAttributes.class);
    private final Validator validator;
    @Context
    ServletContext context;

    public RpcUpdateUserCustomAttributes(Validator validator) {
        this.validator = validator;
    }

    @POST
    @RolesAllowed({"ADMIN"})
    @Path("/{userEmail}")
    public Response rpcUpdateUserCustomAttributes(
            @Context HttpServletRequest req,
            @Context UriInfo uriInfo,
            @Length(min=10, max=128) @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$") @PathParam("userEmail") String userEmail,
            UpdateUserCustomAttributesRequest updateUserCustomAttributesRequest) throws URISyntaxException, CustomException {

        LOGGER.info("POST rpcUpdateUserCustomAttributes email[" + userEmail +
                "] Remote Host[" +
                req.getRemoteHost() +
                "] Remote Address[" + req.getRemoteAddr() +
                "] Remote Port[" + String.valueOf(req.getRemotePort()) + "].");


        try {

            LOGGER.info("POST rpcUpdateUserCustomAttributes Payload[\n" + updateUserCustomAttributesRequest.toString() + "\n].");

            ArrayList<String> validationMessages = new ArrayList<String>();

            // Validaciones mediante Validator
            Set<ConstraintViolation<UpdateUserCustomAttributesRequest>> violations = validator.validate(updateUserCustomAttributesRequest);
            if (violations.size() > 0) {
                for (ConstraintViolation<UpdateUserCustomAttributesRequest> violation : violations) {
                    validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
                }
            }

            // Otras validaciones
            HashMap<String, String> customAttributes = updateUserCustomAttributesRequest.getCustom_attributes();
            if (customAttributes == null || customAttributes.keySet().size() == 0) {
                validationMessages.add("custom_attributes: Se ha enviado una colección NULA o VACIA de 'custom_attributes'.");
            }

            if (validationMessages.size() > 0) {
                ErrorResponse errorResponse = new ErrorResponse(
                        "VAL-ERROR-001",
                        "data-validation-error",
                        validationMessages);
                return Response.status(Status.BAD_REQUEST).entity(errorResponse).build();
            }


            AdminUpdateUserAttributesResponse adminUpdateUserAttributesResponse =
                    adminUpdateUserAttributes(userEmail, updateUserCustomAttributesRequest);
            return Response
                    .status(Status.NO_CONTENT)
                    .build();

        } catch (UserNotFoundException ex) {
            throw new CustomException(
                    Status.NOT_FOUND,
                    "USER-NOT-FOUND",
                    "El usuario ["+ userEmail + "] no existe en el user Pool.",
                    ex);
        } catch (InvalidParameterException ex) {
            throw new CustomException(
                    Status.BAD_REQUEST,
                    "BAD-REQUEST",
                    "Se han enviado atributos/parámetros para el usuario [" + userEmail + "] no incluidos en el esquema configurado en el user Pool.",
                    ex);
        } catch (Throwable ex) {
            throw new CustomException(
                    Status.INTERNAL_SERVER_ERROR,
                    "ERR-500-RPC-UPDATE-USER-CUSTOM-ATTRIBUTES",
                    "process-exception-rpc-update-user-attributes",
                    ex);
        }
    }


    protected static AdminUpdateUserAttributesResponse adminUpdateUserAttributes(
            String userEmail,
            UpdateUserCustomAttributesRequest updateUserCustomAttributesRequest) {

        AdminUpdateUserAttributesResponse AdminUpdateUserAttributesResponse = null;

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                MyAwsBasicCredentials.ACCESS_KEY_ID,
                MyAwsBasicCredentials.SECRET_ACCESS_KEY);

        try ( CognitoIdentityProviderClient cognitoclient = CognitoIdentityProviderClient.builder()
                .region(MyAwsUserPool.REGION)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build()){

            ArrayList<AttributeType> attributeTypeList = new ArrayList<>();

            HashMap<String, String> customAttributes = updateUserCustomAttributesRequest.getCustom_attributes();
            if (customAttributes !=null)
            {
                Set<String> keySet = customAttributes.keySet();
                for (String key: keySet) {
                    attributeTypeList.add(AttributeType.builder().name(key).value(customAttributes.get(key)).build());
                }
            }

            AdminUpdateUserAttributesResponse = cognitoclient.adminUpdateUserAttributes(
                    AdminUpdateUserAttributesRequest.builder()
                            .userPoolId(MyAwsUserPool.USER_POOL_ID)
                            .username(userEmail)
                            .userAttributes(attributeTypeList)
                            .build());

        } catch (Throwable e) {
            throw e;
        }
        return AdminUpdateUserAttributesResponse;
    }

}

