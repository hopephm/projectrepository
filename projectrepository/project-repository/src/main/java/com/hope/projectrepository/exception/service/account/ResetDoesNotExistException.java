package com.hope.projectrepository.exception.service.account;

import com.hope.projectrepository.exception.ExceptionWrapper;

public class ResetDoesNotExistException extends ExceptionWrapper {
    private final static String errorCode = "104";
    public String getErrorCode(){
        return errorCode;
    }
}
