package com.hope.projectrepository.exception.service.file;

import com.hope.projectrepository.exception.ExceptionWrapper;

public class CreateFileResponseEntityException extends ExceptionWrapper {
    private final static String errorCode = "204";
    public String getErrorCode(){
        return errorCode;
    }
}