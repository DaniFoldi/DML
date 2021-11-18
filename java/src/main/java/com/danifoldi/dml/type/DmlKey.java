package com.danifoldi.dml.type;

public class DmlKey extends DmlCommentableValue {
    private String value;

    public DmlKey(String value) {
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
        return "DmlKey{" +
                "value='" + value + '\'' +
                '}';
    }
}
