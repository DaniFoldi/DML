package com.danifoldi.dml.mapper.builtin;

import com.danifoldi.dml.mapper.DmlConverter;
import com.danifoldi.dml.type.DmlCommentableValue;
import com.danifoldi.dml.type.DmlString;

import java.util.UUID;

public class UUIDConverter implements DmlConverter {
    @Override
    public Object convertFrom(DmlCommentableValue dmlValue) {
        return UUID.fromString(dmlValue.asString().value());
    }

    @Override
    public DmlCommentableValue convertTo(Object object, String comment) {
        return new DmlString(object.toString()).withComment(comment);
    }

    @Override
    public DmlCommentableValue fromDefault(String defaultValue, String comment) {
        return new DmlString(defaultValue).withComment(comment);
    }
}