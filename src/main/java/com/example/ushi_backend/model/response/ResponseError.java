package com.example.ushi_backend.model.response;

public class ResponseError extends ResponseData<Void> {
    public ResponseError(int status, String message) {
        super(status, message);
    }
}
