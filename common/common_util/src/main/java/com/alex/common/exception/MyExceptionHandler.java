package com.alex.common.exception;


import com.alex.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(MyException.class)
    public Result handleMyException(MyException e){
        e.printStackTrace();
        return Result.fail("code: "+e.getCode() + ", " +e.getMessage());
    }
}
