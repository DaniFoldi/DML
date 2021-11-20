package com.danifoldi.dml.type;

import com.danifoldi.dml.utils.SerializeUtil;

import java.math.BigDecimal;

public class DmlNumber extends DmlCommentableValue {
    private BigDecimal value;
    private String cachedString;

    public DmlNumber(BigDecimal value) {
        this.value = value;
    }

    public DmlNumber(BigDecimal value, String cachedString) {
        this.value = value;
        this.cachedString = cachedString;
    }

    public BigDecimal value() {
        return this.value;
    }

    public void value(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String serialize(int indent) {
        return SerializeUtil.serializeComment(comment(), indent) + SerializeUtil.indent(cachedString != null ? cachedString : value.toString(), indent);
    }

    @Override
    public String toString() {
        return "DmlNumber{" + value + "}";
    }
}
