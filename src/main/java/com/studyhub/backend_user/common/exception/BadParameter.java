package com.studyhub.backend_user.common.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public class BadParameter extends ClientError{
    public BadParameter(String message) {
        this.errorCode = "Bad Parameter";
        this.errorMessage = message;
    }
}
