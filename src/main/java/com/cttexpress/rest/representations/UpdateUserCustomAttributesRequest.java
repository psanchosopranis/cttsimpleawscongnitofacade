package com.cttexpress.rest.representations;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.util.HashMap;

public class UpdateUserCustomAttributesRequest {

    protected HashMap<String, String> custom_attributes;

    public UpdateUserCustomAttributesRequest() {
    }

    public UpdateUserCustomAttributesRequest(HashMap<String, String> custom_attributes) {
        this.custom_attributes = custom_attributes;
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
