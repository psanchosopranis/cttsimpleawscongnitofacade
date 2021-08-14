package com.cttexpress.rest.auth;


import com.cttexpress.rest.representations.MgmtUser;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/*
==============================================================================
Custom Authenticator Object
==============================================================================
Tomado del Tutorial "HowToDoInJava"
https://howtodoinjava.com/dropwizard/dropwizard-basic-auth-security-example/
By Lokesh Gupta

Authenticator class is responsible for verifying username/password credentials
included in Basic Auth header. In enterprise application, you may fetch the
user’s password from database and if it matches then you set the user roles
into principal object.
In dropwizard, you will need to implements io.dropwizard.auth.Authenticator
interface to put your application logic.
------------------------------------------------------------------------------
*/
public class AppBasicAuthenticator implements Authenticator<BasicCredentials, MgmtPrincipal> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppBasicAuthenticator.class);

    private final MockCredentialsRepo mockCredentialsRepo;

    public AppBasicAuthenticator(MockCredentialsRepo mockCredentialsRepo) {
        this.mockCredentialsRepo = mockCredentialsRepo;
    }

    @Override
    public Optional<MgmtPrincipal> authenticate(BasicCredentials credentials) throws AuthenticationException {

        try {
            MgmtUser mgmtUser = this.mockCredentialsRepo.getMgmtUsersList().get(credentials.getUsername());

            if (mgmtUser == null) {
                LOGGER.error("Rechazo de autorización para el usuario[" + credentials.getUsername() + "]. " +
                        "USUARIO NO RECONOCIDO");
                return Optional.empty();
            }

            if (!mgmtUser.getStatus().equalsIgnoreCase("ACTIVE")) {
                LOGGER.error("Rechazo de autorización para el usuario[" + credentials.getUsername() + "]. " +
                        "USUARIO NO ACTIVO");
                return Optional.empty();
            }

            if (!MgmtUser.passwordMatches(credentials.getPassword(), mgmtUser.getPassword())) {
                LOGGER.error("Rechazo de autorización para el usuario[" + credentials.getUsername() + "]. " +
                        "CREDENCIALES NO VALIDAS");
                return Optional.empty();
            }

            Set<String> roleSet = new HashSet<>(Arrays.asList(mgmtUser.getRoles().split(",")));
            return Optional.of(new MgmtPrincipal(credentials.getUsername(), roleSet));

        } catch (Throwable exception) {
            String mensaje = exception.getClass().getName()
                    + "[ " + (exception.getMessage() != null ? exception.getMessage() : "(mensaje de error no disponible)") + "]";
            Throwable cause = exception.getCause();
            if (cause != null) {
                mensaje +=
                        mensaje + " Causa: "
                                + cause.getClass().getName()
                                + "[ " + (cause.getMessage() != null ? cause.getMessage() : "(mensaje de error no disponible)") + "]";
            }
            LOGGER.error(mensaje);
            return Optional.empty();
        }
    }

}
