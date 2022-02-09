package com.hope.projectrepository.exception;

import com.hope.projectrepository.util.global.Result;

public class ExceptionWrapper extends RuntimeException{
    private final static String errorCode = Result.UnKnownError.getCode();
    public static String getErrorCode(){
        return errorCode;
    }
}
