package com.danifoldi.dml.type;

public class DmlBoolean extends DmlCommentableValue {
    private boolean value;

    public DmlBoolean(boolean value) {
        this.value = value;
    }

    public boolean value() {
        return this.value;
    }

    public void value(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DmlBoolean{" +
                "value=" + value +
                '}';
    }
}
