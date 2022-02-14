package com.hope.projectrepository.exception.service.file;

import com.hope.projectrepository.exception.ExceptionWrapper;

public class OriginFileDoesNotExistException extends ExceptionWrapper {
    private final static String errorCode = "203";
    public String getErrorCode(){
        return errorCode;
    }
}
