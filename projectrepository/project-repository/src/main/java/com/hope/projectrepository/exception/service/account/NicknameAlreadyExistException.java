package com.hope.projectrepository.exception.service.account;

import com.hope.projectrepository.exception.ExceptionWrapper;

public class NicknameAlreadyExistException extends ExceptionWrapper {
    private final static String errorCode = "106";
    public String getErrorCode(){
        return errorCode;
    }
}
