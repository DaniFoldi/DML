package com.danifoldi.dml.exception;

public class DmlMappingException extends RuntimeException {

    public DmlMappingException(String type) {
        super("No mapper was found for type %s".formatted(type));
    }
}
