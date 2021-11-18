package com.danifoldi.dml.type;

public class DmlString extends DmlCommentableValue {
    private String value;

    public DmlString(String value) {
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
        return "DmlString{" +
                "value='" + value + '\'' +
                '}';
    }
}
