package com.hope.projectrepository.exception.service.login;

import com.hope.projectrepository.exception.ExceptionWrapper;

public class LoginIdAlreadyExistException extends ExceptionWrapper {
    private final static String errorCode = "107";
    public String getErrorCode(){
        return errorCode;
    }
}