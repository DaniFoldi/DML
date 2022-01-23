package com.danifoldi.dml.exception;

public class DmlParseException extends Exception {

    public DmlParseException(int line, int column, String expect, String found) {
        super("DML parsing failed at line %d:%d (Expected %s, found %s)".formatted(line, column, expect, found));
    }
}
