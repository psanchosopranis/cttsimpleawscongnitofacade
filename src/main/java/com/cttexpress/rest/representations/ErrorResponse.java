package com.cttexpress.rest.representations;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;

public class ErrorResponse {
    protected String errorId;
    protected String errorAlias;
    protected ArrayList<String> errorMessages;

    public ErrorResponse() {
    }

    public ErrorResponse(String errorId, String errorAlias, ArrayList<String> errorMessages) {
        this.errorId = errorId;
        this.errorAlias = errorAlias;
        this.errorMessages = errorMessages;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getErrorAlias() {
        return errorAlias;
    }

    public void setErrorAlias(String errorAlias) {
        this.errorAlias = errorAlias;
    }

    public ArrayList<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(ArrayList<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
