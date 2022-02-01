package com.danifoldi.dml;

public class DmlTypeException extends RuntimeException {

    public DmlTypeException(String expectedType, String gotType) {

        super("Expected type %s, got %s".formatted(expectedType, gotType));
    }
}
