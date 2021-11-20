package com.danifoldi.dml.type;

import com.danifoldi.dml.utils.SerializeUtil;

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
    public String serialize(int indent) {
        return SerializeUtil.serializeComment(comment(), indent) + SerializeUtil.indent(String.valueOf(value), indent);
    }

    @Override
    public String toString() {
        return "DmlBoolean{" + value + "}";
    }
}
