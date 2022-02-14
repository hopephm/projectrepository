package com.hope.projectrepository.exception;

import com.hope.projectrepository.util.global.Result;

public abstract class ExceptionWrapper extends RuntimeException{
    public abstract String getErrorCode();
}
