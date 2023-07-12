package com.articoding.error;

public class ErrorNotFound extends RestError {
    public ErrorNotFound(String entity, Long id) {
        super(String.format("%s con identificador %d no existe", entity, id));
    }
}
