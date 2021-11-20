package com.danifoldi.dml.type;

import com.danifoldi.dml.utils.SerializeUtil;

import java.util.List;
import java.util.stream.Collectors;

public class DmlArray extends DmlCommentableValue implements DmlDocument {
    private List<DmlValue> value;

    public DmlArray(List<DmlValue> value) {
        this.value = value;
    }

    public List<DmlValue> value() {
        return this.value;
    }

    public void value(List<DmlValue> value) {
        this.value = value;
    }

    public void add(DmlValue value) {
        this.value.add(value);
    }

    @Override
    public String serialize(int indent) {
        String dense = value.stream().map(v -> v.serialize(0)).collect(Collectors.joining(", "));
        return SerializeUtil.serializeComment(comment(), indent) + (!dense.contains("\n") && dense.length() <= 80 ? (value.isEmpty() ? "[]" : "[ " + dense + " ]") : SerializeUtil.indent("[\n" + value.stream().map(v -> v.serialize(2)).collect(Collectors.joining("\n")) + "\n]", indent));
    }

    @Override
    public String serializeDocument() {
        return serialize(0);
    }

    @Override
    public String toString() {
        return "DmlArray{" + value + "}";
    }
}
