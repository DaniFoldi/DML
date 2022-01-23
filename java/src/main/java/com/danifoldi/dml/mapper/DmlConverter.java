package com.danifoldi.dml.mapper;

import com.danifoldi.dml.type.DmlCommentableValue;

public interface DmlConverter {
    Object convertFrom(DmlCommentableValue dmlValue);
    DmlCommentableValue convertTo(Object object, String comment);
    DmlCommentableValue fromDefault(String defaultValue, String comment);
}
