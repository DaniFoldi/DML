package com.danifoldi.dml.type;

import java.util.Map;

public class DmlObject extends DmlCommentableValue {
    private Map<DmlKey, DmlValue> value;

    public DmlObject(Map<DmlKey, DmlValue> value) {
        this.value = value;
    }

    public Map<DmlKey, DmlValue> value() {
        return this.value;
    }

    public void value(Map<DmlKey, DmlValue> value) {
        this.value = value;
    }

    public void add(DmlKey key, DmlValue value) {
        this.value.put(key, value);
    }

    @Override
    public String toString() {
        return "DmlObject{" +
                "value=" + value +
                '}';
    }
}
