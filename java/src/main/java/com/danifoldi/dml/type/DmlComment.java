package com.danifoldi.dml.type;

public class DmlComment extends DmlValue {
    private String value;

    public DmlComment(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public void value(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DmlComment{" +
                "value='" + value + '\'' +
                '}';
    }
}
