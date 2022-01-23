package com.danifoldi.dml.mapper.builtin;

import com.danifoldi.dml.mapper.DmlConverter;
import com.danifoldi.dml.type.DmlCommentableValue;
import com.danifoldi.dml.type.DmlNumber;

import java.math.BigDecimal;

public class FloatConverter implements DmlConverter {
    @Override
    public Object convertFrom(DmlCommentableValue dmlValue) {
        return dmlValue.asNumber().value().floatValue();
    }

    @Override
    public DmlCommentableValue convertTo(Object object, String comment) {
        return new DmlNumber(BigDecimal.valueOf((Float) object), object.toString()).withComment(comment);
    }

    @Override
    public DmlCommentableValue fromDefault(String defaultValue, String comment) {
        return new DmlNumber(new BigDecimal(defaultValue), defaultValue).withComment(comment);
    }
}