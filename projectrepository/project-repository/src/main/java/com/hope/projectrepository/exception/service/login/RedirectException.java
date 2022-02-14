package com.hope.projectrepository.exception.service.login;

import com.hope.projectrepository.exception.ExceptionWrapper;

public class RedirectException extends ExceptionWrapper {
    private final static String errorCode = "110";
    public String getErrorCode(){
        return errorCode;
    }
}
