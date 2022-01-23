package com.danifoldi.dml.mapper.builtin;

import com.danifoldi.dml.mapper.DmlConverter;
import com.danifoldi.dml.type.DmlArray;
import com.danifoldi.dml.type.DmlCommentableValue;
import com.danifoldi.dml.type.DmlNumber;
import com.danifoldi.dml.type.DmlValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ShortSetConverter implements DmlConverter {
    @Override
    public Object convertFrom(DmlCommentableValue dmlValue) {
        return dmlValue.asArray().value().stream().map(DmlValue::asNumber).map(v -> v.value().shortValue()).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public DmlCommentableValue convertTo(Object object, String comment) {
        return new DmlArray(new ArrayList<>(((Set<Short>)object).stream().map(v -> (DmlValue)new DmlNumber(new BigDecimal(v))).toList())).withComment(comment);
    }

    @Override
    public DmlCommentableValue fromDefault(String defaultValue, String comment) {
        return new DmlArray(new ArrayList<>((Arrays.stream(defaultValue.split(",")).map(v -> (DmlValue)new DmlNumber(new BigDecimal(v), v)).toList()))).withComment(comment);
    }
}