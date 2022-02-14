package com.hope.projectrepository.exception.service.mail;

import com.hope.projectrepository.exception.ExceptionWrapper;

public class EmailSendException extends ExceptionWrapper {
    private final static String errorCode = "301";
    public String getErrorCode(){
        return errorCode;
    }
}
