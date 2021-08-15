package com.cttexpress.rest.representations;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.HashMap;

public class RetrieveUserMetadataResponse {

    protected String user_id;
    protected String user_status;
    protected boolean user_is_enabled;
    protected String user_create_date;
    protected String user_last_modified_date;
    protected HashMap<String, String> user_attributes;

    public RetrieveUserMetadataResponse() {
        this.user_attributes = new HashMap<>();
    }

    public RetrieveUserMetadataResponse(String user_id, String user_status, boolean user_is_enabled, String user_create_date, String user_last_modified_date, HashMap<String, String> user_attributes) {
        this.user_id = user_id;
        this.user_status = user_status;
        this.user_is_enabled = user_is_enabled;
        this.user_create_date = user_create_date;
        this.user_last_modified_date = user_last_modified_date;
        this.user_attributes = user_attributes;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public boolean isUser_is_enabled() {
        return user_is_enabled;
    }

    public void setUser_is_enabled(boolean user_is_enabled) {
        this.user_is_enabled = user_is_enabled;
    }

    public String getUser_create_date() {
        return user_create_date;
    }

    public void setUser_create_date(String user_create_date) {
        this.user_create_date = user_create_date;
    }

    public String getUser_last_modified_date() {
        return user_last_modified_date;
    }

    public void setUser_last_modified_date(String user_last_modified_date) {
        this.user_last_modified_date = user_last_modified_date;
    }

    public HashMap<String, String> getUser_attributes() {
        return user_attributes;
    }

    public void setUser_attributes(HashMap<String, String> user_attributes) {
        this.user_attributes = user_attributes;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
