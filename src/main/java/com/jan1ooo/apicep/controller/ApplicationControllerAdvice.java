package com.jan1ooo.apicep.controller;

import com.jan1ooo.apicep.exception.NoContentException;
import com.jan1ooo.apicep.exception.NotReadyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(NoContentException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String handleNoContentException(NoContentException msg){
        return msg.getMessage();
    }

    @ExceptionHandler(NotReadyException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleNotReadyException(NotReadyException msg){
        return msg.getMessage();
    }
}
