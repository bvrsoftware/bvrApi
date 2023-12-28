package com.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private HttpStatus status = HttpStatus.OK;
    private int code = HttpStatus.OK.value();
    private List<ApiFieldError> errors;
    private String message;
    private T data;
    private Exception exception;

    public ApiResponse() {
        super();
    }
    public ApiResponse(T data) {
        super();
        this.data = data;
    }
    public ApiResponse(List<ApiFieldError> errors) {
        super();
        this.errors = errors;
        this.status = HttpStatus.BAD_REQUEST;
        this.code = HttpStatus.BAD_REQUEST.value();
    }
    public ApiResponse(HttpStatus httpStatus, String message) {
        super();
        this.status = httpStatus;
        this.code = httpStatus.value();
        this.message = message;
    }
    public ApiResponse(HttpStatus httpStatus, Exception exception) {
        super();
        this.status = httpStatus;
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
        this.exception = exception;

    }
    public HttpStatus getStatus() {
        return status;
    }
    public void setStatus(HttpStatus status) {
        this.status = status;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public List<ApiFieldError> getErrors() {
        return errors;
    }
    public void setErrors(List<ApiFieldError> errors) {
        this.errors = errors;
    }
    public Exception getException() {
        return exception;
    }
    public void setException(Exception exception) {
        this.exception = exception;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

}
