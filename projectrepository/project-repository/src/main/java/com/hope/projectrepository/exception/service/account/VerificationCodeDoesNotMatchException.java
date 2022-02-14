package com.hope.projectrepository.exception.service.account;

import com.hope.projectrepository.exception.ExceptionWrapper;

public class VerificationCodeDoesNotMatchException extends ExceptionWrapper {
    private final static String errorCode = "101";
    public String getErrorCode(){
        return errorCode;
    }
}
