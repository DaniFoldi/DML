package com.danifoldi.dml.type;

import com.danifoldi.dml.DmlSerializer;

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
    public String serialize(int indent) {
        return DmlSerializer.serializeComment(comment(), indent) + DmlSerializer.indent("'" + value.replace("'", "\\'") + "'", indent);
    }

    @Override
    public String toString() {
        return "DmlString{" + value + "}";
    }
}
