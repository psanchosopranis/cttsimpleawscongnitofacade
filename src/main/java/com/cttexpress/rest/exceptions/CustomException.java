package com.cttexpress.rest.exceptions;

import javax.ws.rs.core.Response;

public class CustomException extends Throwable {
    protected Response.Status code;
    protected String errorId;
    protected String errorAlias;

    public CustomException(
            Response.Status code,
            String errorId,
            String errorAlias,
            String message
    ) {
        super(message);
        this.code = code;
        this.errorId = errorId;
        this.errorAlias = errorAlias;
    }

    public CustomException(
            Response.Status code,
            String errorId,
            String errorAlias,
            Throwable throwable
    ) {
        super(throwable);
        this.code = code;
        this.errorId = errorId;
        this.errorAlias = errorAlias;
    }

    public CustomException(
            Response.Status code,
            String errorId,
            String errorAlias,
            String message,
            Throwable throwable
            ) {
        super(message, throwable);
        this.code = code;
        this.errorId = errorId;
        this.errorAlias = errorAlias;
    }

    public Response.Status getCode() {
        return code;
    }

    public String getErrorId() {
        return errorId;
    }

    public String getErrorAlias() {
        return errorAlias;
    }

}
