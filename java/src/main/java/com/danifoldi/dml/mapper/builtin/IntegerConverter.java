package com.danifoldi.dml.mapper.builtin;

import com.danifoldi.dml.mapper.DmlConverter;
import com.danifoldi.dml.type.DmlCommentableValue;
import com.danifoldi.dml.type.DmlNumber;

import java.math.BigDecimal;

public class IntegerConverter implements DmlConverter {
    @Override
    public Object convertFrom(DmlCommentableValue dmlValue) {
        return dmlValue.asNumber().value().intValue();
    }

    @Override
    public DmlCommentableValue convertTo(Object object, String comment) {
        return new DmlNumber(new BigDecimal((Integer)object), object.toString()).withComment(comment);
    }

    @Override
    public DmlCommentableValue fromDefault(String defaultValue, String comment) {
        return new DmlNumber(new BigDecimal(defaultValue), defaultValue).withComment(comment);
    }
}
