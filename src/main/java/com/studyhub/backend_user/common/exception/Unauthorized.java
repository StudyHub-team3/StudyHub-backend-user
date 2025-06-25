package com.studyhub.backend_user.common.exception;

public class Unauthorized extends ClientError {
    public Unauthorized(String message) {
        this.errorCode = "Unauthorized";
        this.errorMessage = message;
    }
}
