package com.cttexpress;

import com.cttexpress.config.AppHealthCheck;
import com.cttexpress.rest.auth.*;
import com.cttexpress.rest.exceptions.CustomConstraintViolationExceptionMapper;
import com.cttexpress.rest.exceptions.CustomExceptionMapper;
import com.cttexpress.rest.exceptions.CustomThrowableExceptionMapper;
import com.cttexpress.rest.resources.*;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import com.cttexpress.config.MainConfiguration;


import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 */
public class MainApplication extends Application<MainConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);
    private static final String[] VALID_ROLES = new String[] {"ADMIN", "SYSTEM", "NONE" };

    public static void main(String[] args) throws Exception {
        new MainApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<MainConfiguration> bootstrap) {
        LOGGER.info("Inicializando la aplicación ...");
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor())
        );
    }

    @Override
    public void run(MainConfiguration configuration, Environment environment) throws Exception {

        LOGGER.info("Inicializando Usuarios de Gestión ...");
        MockCredentialsRepo mockCredentialsRepo = new MockCredentialsRepo();


        LOGGER.info("Registrando recursos ...");
        environment.jersey().register(new RpcForceUserPasswordReset(environment.getValidator()));
        environment.jersey().register(new RpcDisableUser(environment.getValidator()));
        environment.jersey().register(new RpcEnableUser(environment.getValidator()));
        environment.jersey().register(new RpcDeleteUser(environment.getValidator()));
        environment.jersey().register(new RpcCreateUser(environment.getValidator()));
        environment.jersey().register(new RpcUpdateUserAttributes(environment.getValidator()));

        LOGGER.info("Registrando recursos de Healthcheck ...");
        //Application health check
        environment.healthChecks().register("cttsimpleawscognitofacade", new AppHealthCheck());

        LOGGER.info("Registrando  CustomExceptionMapper...");
        environment.jersey().register(new CustomExceptionMapper());
        environment.jersey().register(new CustomThrowableExceptionMapper());
        environment.jersey().register(new CustomConstraintViolationExceptionMapper());

        //****** Dropwizard security - custom classes ***********/
        LOGGER.info("Registrando BasicCredentialAuthFilter ...");
        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<MgmtPrincipal>()
                .setAuthenticator(new AppBasicAuthenticator(mockCredentialsRepo))
                .setAuthorizer(new AppAuthorizer())
                .setRealm("cttexpresssimpleiam")
                .setUnauthorizedHandler(new CustomUnauthorizedHandler())
                .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(MgmtPrincipal.class));
    }
}