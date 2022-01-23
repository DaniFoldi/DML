package com.danifoldi.dml.mapper.builtin;

import com.danifoldi.dml.mapper.DmlConverter;
import com.danifoldi.dml.type.DmlArray;
import com.danifoldi.dml.type.DmlCommentableValue;
import com.danifoldi.dml.type.DmlString;
import com.danifoldi.dml.type.DmlValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UUIDListConverter implements DmlConverter {
    @Override
    public Object convertFrom(DmlCommentableValue dmlValue) {
        return dmlValue.asArray().value().stream().map(DmlValue::asString).map(v -> UUID.fromString(v.value())).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public DmlCommentableValue convertTo(Object object, String comment) {
        return new DmlArray(new ArrayList<>(((List<UUID>)object).stream().map(v -> (DmlValue)new DmlString(v.toString())).toList())).withComment(comment);
    }

    @Override
    public DmlCommentableValue fromDefault(String defaultValue, String comment) {
        return new DmlArray(new ArrayList<>((Arrays.stream(defaultValue.split(",")).map(v -> (DmlValue)new DmlString(v)).toList()))).withComment(comment);
    }
}