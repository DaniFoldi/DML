package com.danifoldi.dml.mapper.builtin;

import com.danifoldi.dml.mapper.DmlConverter;
import com.danifoldi.dml.type.DmlBoolean;
import com.danifoldi.dml.type.DmlCommentableValue;

public class BooleanConverter implements DmlConverter {
    @Override
    public Object convertFrom(DmlCommentableValue dmlValue) {
        return dmlValue.asBoolean().value();
    }

    @Override
    public DmlCommentableValue convertTo(Object object, String comment) {
        return new DmlBoolean(Boolean.parseBoolean(object.toString())).withComment(comment);
    }

    @Override
    public DmlCommentableValue fromDefault(String defaultValue, String comment) {
        return new DmlBoolean(Boolean.parseBoolean(defaultValue)).withComment(comment);
    }
}