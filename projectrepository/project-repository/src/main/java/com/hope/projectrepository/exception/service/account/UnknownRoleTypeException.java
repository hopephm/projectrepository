package com.hope.projectrepository.exception.service.account;

import com.hope.projectrepository.exception.ExceptionWrapper;

public class UnknownRoleTypeException extends ExceptionWrapper {
    private final static String errorCode = "109";
    public String getErrorCode(){
        return errorCode;
    }
}
