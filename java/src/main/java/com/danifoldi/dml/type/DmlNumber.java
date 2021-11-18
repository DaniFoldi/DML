package com.danifoldi.dml.type;

import java.math.BigDecimal;

public class DmlNumber extends DmlCommentableValue {
    private BigDecimal value;

    public DmlNumber(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal value() {
        return this.value;
    }

    public void value(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DmlNumber{" +
                "value=" + value +
                '}';
    }
}
