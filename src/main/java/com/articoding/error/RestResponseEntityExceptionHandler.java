package com.articoding.error;

import com.articoding.model.rest.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = { NotAuthorization.class })
    protected ResponseEntity<Object> authorizationError(
            RuntimeException ex, WebRequest request) {
        RestError restError = (RestError)ex;
        return new ResponseEntity<>(new ErrorMessage(restError.getRestMesssage(), 1), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(value
            = { ErrorNotFound.class })
    protected ResponseEntity<Object> notFountError(
            RuntimeException ex, WebRequest request) {
        RestError restError = (RestError)ex;
        return new ResponseEntity<>(new ErrorMessage(restError.getRestMesssage(), 1), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value
            = { DisabledEntity.class })
    protected ResponseEntity<Object> disableEntity(
            RuntimeException ex, WebRequest request) {
        RestError restError = (RestError)ex;
        return new ResponseEntity<>(new ErrorMessage(restError.getRestMesssage(), 1), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}