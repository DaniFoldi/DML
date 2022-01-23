package com.danifoldi.dml.mapper.builtin;

import com.danifoldi.dml.mapper.DmlConverter;
import com.danifoldi.dml.type.DmlCommentableValue;
import com.danifoldi.dml.type.DmlString;

public class CharacterConverter implements DmlConverter {
    @Override
    public Object convertFrom(DmlCommentableValue dmlValue) {
        return dmlValue.asString().value().charAt(0);
    }

    @Override
    public DmlCommentableValue convertTo(Object object, String comment) {
        return new DmlString(((Character)object).toString()).withComment(comment);
    }

    @Override
    public DmlCommentableValue fromDefault(String defaultValue, String comment) {
        return new DmlString(defaultValue).withComment(comment);
    }
}