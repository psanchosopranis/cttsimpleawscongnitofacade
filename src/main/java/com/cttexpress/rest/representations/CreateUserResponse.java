package com.cttexpress.rest.representations;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.util.HashMap;

public class CreateUserResponse {

    protected String email;
    protected String user_internal_id;

    public CreateUserResponse() {
    }

    public CreateUserResponse(String email, String user_internal_id) {
        this.email = email;
        this.user_internal_id = user_internal_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_internal_id() {
        return user_internal_id;
    }

    public void setUser_internal_id(String user_internal_id) {
        this.user_internal_id = user_internal_id;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
