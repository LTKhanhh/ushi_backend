package com.example.ushi_backend.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseData<T> {

    private int status;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // GET, POST
    public ResponseData(T data, int status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    // PUT, PATCH, DELETE
    public ResponseData(int status, String message) {
        this.message = message;
        this.status = status;
    }
}
