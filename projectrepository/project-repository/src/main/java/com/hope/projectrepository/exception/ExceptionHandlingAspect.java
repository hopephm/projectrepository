package com.hope.projectrepository.exception;

import com.hope.projectrepository.util.global.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlingAspect {
    @Around("@annotation(com.hope.projectrepository.exception.ExceptionHandling)")
    public String handleExceptions(ProceedingJoinPoint joinPoint) throws Throwable{
        try{
            return (String)joinPoint.proceed();
        }catch(ExceptionWrapper e){
            ExceptionHandler.handle(e);
            return e.getErrorCode();
        }catch(Exception e){
            ExceptionHandler.handle(e);
            return Result.UnKnownError.getCode();
        }
    }
}
