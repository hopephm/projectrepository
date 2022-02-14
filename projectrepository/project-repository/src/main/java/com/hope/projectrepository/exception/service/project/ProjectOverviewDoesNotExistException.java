package com.hope.projectrepository.exception.service.project;

import com.hope.projectrepository.exception.ExceptionWrapper;

public class ProjectOverviewDoesNotExistException extends ExceptionWrapper {
    private final static String errorCode = "401";
    public String getErrorCode(){
        return errorCode;
    }
}
