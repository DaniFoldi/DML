package com.danifoldi.dml.type;

import java.util.List;

public class DmlArray extends DmlCommentableValue {
    private List<DmlValue> value;

    public DmlArray(List<DmlValue> value) {
        this.value = value;
    }

    public List<DmlValue> value() {
        return this.value;
    }

    public void value(List<DmlValue> value) {
        this.value = value;
    }

    public void add(DmlValue value) {
        this.value.add(value);
    }

    @Override
    public String toString() {
        return "DmlArray{" +
                "value=" + value +
                '}';
    }
}
