package com.cttexpress.rest.representations;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.mindrot.jbcrypt.BCrypt;

public class MgmtUser {

    @NotBlank @Length(min=5, max=64)
    private String name;
    @NotBlank @Length(min=8, max=128)
    private String password;
    @NotBlank @Length(min=4, max=72)
    private String roles;
    @NotBlank @Length(min=6, max=45)
    private String status;
    @NotBlank @Length(min=10, max=72)
    private String lastUpdate;

    public MgmtUser() {
    }

    public MgmtUser(
            String name,
            String password, // NOTA: YA ENCRIPTADA CON BCRYPT
            String roles,
            String status,
            String lastUpdate) {
        this.name = name;
        this.password = password;
        this.roles = roles;
        this.status = status;
        this.lastUpdate = lastUpdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public static boolean passwordMatches(String clearPassword, String storedPassword) {
        return BCrypt.checkpw(clearPassword, storedPassword);
    }

    public static String bcryptPassword(String clearPassword) {
        return BCrypt.hashpw(clearPassword, BCrypt.gensalt());
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}