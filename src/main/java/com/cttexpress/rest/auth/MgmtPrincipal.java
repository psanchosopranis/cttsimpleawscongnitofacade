package com.cttexpress.rest.auth;

import java.security.Principal;
import java.util.Set;

/*
==============================================================================
Custom Principal Object
==============================================================================
En una primera fase se utilizará un modelo de BASIC AUTH para proteger
algunos de los recursos basado en usuarios y roles "hard-coded" con el
objeto de acelerar la disponibilidad de la solución.

------------------------------------------------------------------------------
Tomado del Tutorial "HowToDoInJava"
https://howtodoinjava.com/dropwizard/dropwizard-basic-auth-security-example/
By Lokesh Gupta
------------------------------------------------------------------------------
*/

public class MgmtPrincipal implements Principal {
    private final String name;

    private final Set<String> roles;

    public MgmtPrincipal(String name) {
        this.name = name;
        this.roles = null;
    }

    public MgmtPrincipal(String name, Set<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return (int) (Math.random() * 100);
    }

    public Set<String> getRoles() {
        return roles;
    }
}
