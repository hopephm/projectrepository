package com.hope.projectrepository.exception.handle;

import com.hope.projectrepository.exception.ExceptionWrapper;
import com.hope.projectrepository.exception.global.ExceptionHandler;
import com.hope.projectrepository.util.global.Result;
import com.hope.projectrepository.util.response.json.JsonResponseWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlingAspect {
    @Around("@annotation(com.hope.projectrepository.exception.handle.ExceptionHandling)")
    public String handleExceptions(ProceedingJoinPoint joinPoint) throws Throwable{
        JsonResponseWrapper jr = new JsonResponseWrapper();

        try{
            return (String) joinPoint.proceed();
        }catch(Exception e){
            ExceptionHandler.handle(e);

            String errorCode = Result.UnKnownError.getCode();
            if(e instanceof ExceptionWrapper){
                ExceptionWrapper ew = (ExceptionWrapper) e;
                errorCode = ew.getErrorCode();
            }

            jr.setResponseCode(errorCode);
            return jr.getResponse();
        }
    }
}
