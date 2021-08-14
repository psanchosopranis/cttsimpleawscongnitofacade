package com.cttexpress.rest.auth;

import io.dropwizard.auth.Authorizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
==============================================================================
Custom Authorizer
==============================================================================
Tomado del Tutorial "HowToDoInJava"
https://howtodoinjava.com/dropwizard/dropwizard-basic-auth-security-example/
By Lokesh Gupta

Authorizer class is responsible for matching the roles and decide whether user
is allowed to perform certain action or not.
------------------------------------------------------------------------------
*/
public class AppAuthorizer implements Authorizer<MgmtPrincipal> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppAuthorizer.class);

    @Override
    public boolean authorize(MgmtPrincipal user, String role) {
        if (user.getRoles() != null && user.getRoles().contains(role)) {
            LOGGER.info("Autorización otorgada para el usuario[" + user.getName() + "].");
            return true;
        } else {
            LOGGER.error("Rechazo de autorización para el usuario[" + user.getName() + "] -> No dispone del ROL requerido.");
            return false;
        }

    }
}