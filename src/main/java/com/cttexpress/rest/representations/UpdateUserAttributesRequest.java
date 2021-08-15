package com.cttexpress.rest.representations;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.util.HashMap;

public class UpdateUserAttributesRequest {

    @NotBlank @Length(min=5, max=64)
    protected String name;
    @NotBlank @Length(min=12, max=13)
    @Pattern(regexp = "^\\+\\d{11,12}$")
    protected String phone_number;
    protected HashMap<String, String> custom_attributes;

    public UpdateUserAttributesRequest() {
    }

    public UpdateUserAttributesRequest(String name, String phone_number, HashMap<String, String> custom_attributes) {
        this.name = name;
        this.phone_number = phone_number;
        this.custom_attributes = custom_attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
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
