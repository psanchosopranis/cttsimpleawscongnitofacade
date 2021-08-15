package com.cttexpress.rest.resources;

import com.cttexpress.config.MyAwsBasicCredentials;
import com.cttexpress.config.MyAwsUserPool;
import com.cttexpress.rest.exceptions.CustomException;
import com.cttexpress.rest.representations.CreateUserRequest;
import com.cttexpress.rest.representations.CreateUserResponse;
import com.cttexpress.rest.representations.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UsernameExistsException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InvalidParameterException;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
    @RolesAllowed({"ADMIN"})
    public Response rpcCreateUser(
            @Context HttpServletRequest req,
            @Context UriInfo uriInfo,
            CreateUserRequest createUserRequest) throws URISyntaxException, CustomException {

        LOGGER.info("POST rpcCreateUser Remote Host[" +
                req.getRemoteHost() +
                "] Remote Address[" + req.getRemoteAddr() +
                "] Remote Port[" + String.valueOf(req.getRemotePort()) + "].");


        try {

            LOGGER.info("POST rpcCreateUser Payload[\n" + createUserRequest.toString() + "\n].");

            ArrayList<String> validationMessages = new ArrayList<String>();

            // Validaciones mediante Validator
            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);
            if (violations.size() > 0) {
                for (ConstraintViolation<CreateUserRequest> violation : violations) {
                    validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
                }
            }

            if (validationMessages.size() > 0) {
                ErrorResponse errorResponse = new ErrorResponse(
                        "VAL-ERROR-001",
                        "data-validation-error",
                        validationMessages);
                return Response.status(Status.BAD_REQUEST).entity(errorResponse).build();
            }

            CreateUserResponse createUserResponse = new CreateUserResponse();
            AdminCreateUserResponse adminCreateUserResponse = adminCreateUser(createUserRequest);
            createUserResponse.setUser_internal_id(adminCreateUserResponse.user().username());
            createUserResponse.setEmail(createUserRequest.getEmail());
            return Response
                    .status(Status.CREATED)
                    .entity(createUserResponse)
                    .build();

        } catch (UsernameExistsException ex) {
            throw new CustomException(
                    Status.CONFLICT,
                    "USER-WITH-SAME-EMAIL-ALREADY-EXISTS",
                    "Ya existe un usuario con el mismo eMail [" + createUserRequest.getEmail() + "] en el user Pool.",
                    ex);

        } catch (InvalidParameterException ex) {
            throw new CustomException(
                    Status.BAD_REQUEST,
                    "BAD-REQUEST",
                    "Se han enviado atributos/parámetros para el usuario [" + createUserRequest.getEmail() + "] no incluidos en el esquema configurado en el user Pool.",
                    ex);
        } catch (Throwable ex) {
            throw new CustomException(
                    Status.INTERNAL_SERVER_ERROR,
                    "ERR-500-RPC-CREATE-USER",
                    "process-exception-rpc-create-user",
                    ex);
        }
    }

    protected static AdminCreateUserResponse adminCreateUser(CreateUserRequest createUserRequest) {

        AdminCreateUserResponse AdminCreateUserResponse = null;

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                MyAwsBasicCredentials.ACCESS_KEY_ID,
                MyAwsBasicCredentials.SECRET_ACCESS_KEY);

        try ( CognitoIdentityProviderClient cognitoclient = CognitoIdentityProviderClient.builder()
                .region(MyAwsUserPool.REGION)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build()){

            ArrayList<AttributeType> attributeTypeList = new ArrayList<>();
            attributeTypeList.add(AttributeType.builder().name("name").value(createUserRequest.getName()).build());
            attributeTypeList.add(AttributeType.builder().name("email").value(createUserRequest.getEmail()).build());
            attributeTypeList.add(AttributeType.builder().name("email_verified").value("true").build());
            attributeTypeList.add(AttributeType.builder().name("phone_number").value(createUserRequest.getPhone_number()).build());
            attributeTypeList.add(AttributeType.builder().name("phone_number_verified").value("true").build());

            HashMap<String, String> customAttributes = createUserRequest.getCustom_attributes();
            if (customAttributes !=null)
            {
                Set<String> keySet = customAttributes.keySet();
                for (String key: keySet) {
                    attributeTypeList.add(AttributeType.builder().name(key).value(customAttributes.get(key)).build());
                }
            }

            AdminCreateUserResponse = cognitoclient.adminCreateUser(
                    AdminCreateUserRequest.builder()
                            .userPoolId(MyAwsUserPool.USER_POOL_ID)
                            .desiredDeliveryMediums(DeliveryMediumType.EMAIL)
                            .username(createUserRequest.getEmail())
                            .userAttributes(attributeTypeList)
                            .build());

        } catch (Throwable e) {
            throw e;
        }
        return AdminCreateUserResponse;
    }

}

