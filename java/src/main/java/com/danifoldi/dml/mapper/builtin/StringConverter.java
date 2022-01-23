package com.danifoldi.dml.mapper.builtin;

import com.danifoldi.dml.mapper.DmlConverter;
import com.danifoldi.dml.type.DmlCommentableValue;
import com.danifoldi.dml.type.DmlString;

public class StringConverter implements DmlConverter {
    @Override
    public Object convertFrom(DmlCommentableValue dmlValue) {
        return dmlValue.asString().value();
    }

    @Override
    public DmlCommentableValue convertTo(Object object, String comment) {
        return new DmlString((String)object).withComment(comment);
    }

    @Override
    public DmlCommentableValue fromDefault(String defaultValue, String comment) {
        return new DmlString(defaultValue).withComment(comment);
    }
}