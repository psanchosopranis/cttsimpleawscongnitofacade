package com.cttexpress.rest.representations;

import java.util.HashMap;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Pattern;

public class CreateUserRequest {

    @Length(min=10, max=128)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    protected String email;
    @NotBlank @Length(min=5, max=64)
    protected String name;
    protected boolean email_verified;
    @Length(min=12, max=32)
    @Pattern(regexp = "^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$")
    protected String phone_number;
    protected boolean phone_number_verified;
    protected HashMap<String, String> custom_attributes;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String email, String name, boolean email_verified, String phone_number, boolean phone_number_verified, HashMap<String, String> custom_attributes) {
        this.email = email;
        this.name = name;
        this.email_verified = email_verified;
        this.phone_number = phone_number;
        this.phone_number_verified = phone_number_verified;
        this.custom_attributes = custom_attributes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEmail_verified() {
        return email_verified;
    }

    public void setEmail_verified(boolean email_verified) {
        this.email_verified = email_verified;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isPhone_number_verified() {
        return phone_number_verified;
    }

    public void setPhone_number_verified(boolean phone_number_verified) {
        this.phone_number_verified = phone_number_verified;
    }

    public HashMap<String, String> getCustom_attributes() {
        return custom_attributes;
    }

    public void setCustom_attributes(HashMap<String, String> custom_attributes) {
        this.custom_attributes = custom_attributes;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
