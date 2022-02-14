package com.hope.projectrepository.exception.service.account;

import com.hope.projectrepository.exception.ExceptionWrapper;

public class EmailVerificationTimeOutException extends ExceptionWrapper {
    private final static String errorCode = "108";
    public String getErrorCode(){
        return errorCode;
    }
}
