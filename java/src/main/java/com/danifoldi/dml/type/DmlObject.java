package com.danifoldi.dml.type;

import com.danifoldi.dml.DmlSerializer;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DmlObject extends DmlCommentableValue implements DmlDocument {
    private Map<DmlKey, DmlValue> value;

    public DmlObject(Map<DmlKey, DmlValue> value) {
        this.value = value;
    }

    public void set(DmlKey key, DmlValue value) {
        this.value.put(key, value);
    }

    public void set(String key, DmlValue value) {
        this.value.put(new DmlKey(key), value);
    }

    public DmlValue get(DmlKey key) {
        return this.value.get(key);
    }

    public DmlArray getArray(DmlKey key) {
        return this.value.get(key).asArray();
    }

    public DmlBoolean getBoolean(DmlKey key) {
        return this.value.get(key).asBoolean();
    }

    public DmlNumber getNumber(DmlKey key) {
        return this.value.get(key).asNumber();
    }

    public DmlObject getObject(DmlKey key) {
        return this.value.get(key).asObject();
    }

    public DmlString getString(DmlKey key) {
        return this.value.get(key).asString();
    }

    public Set<DmlKey> keys() {
        return this.value.keySet();
    }

    public DmlValue get(String key) {
        return this.value.get(new DmlKey(key));
    }

    public DmlArray getArray(String key) {
        return this.value.get(new DmlKey(key)).asArray();
    }

    public DmlBoolean getBoolean(String key) {
        return this.value.get(new DmlKey(key)).asBoolean();
    }

    public DmlNumber getNumber(String key) {
        return this.value.get(new DmlKey(key)).asNumber();
    }

    public DmlObject getObject(String key) {
        return this.value.get(new DmlKey(key)).asObject();
    }

    public DmlString getString(String key) {
        return this.value.get(new DmlKey(key)).asString();
    }

    @Override
    public String serialize(int indent) {
        String dense = value.keySet().stream().map(v -> v.serialize(2) + ": " + value.get(v).serialize(0)).collect(Collectors.joining(", "));
        return DmlSerializer.serializeComment(comment(), indent) + (!dense.contains("\n") && dense.length() <= 80 ? (value.isEmpty() ? "{}" : "{ " + dense + " }") : DmlSerializer.indent("{\n" + value.keySet().stream().sorted().map(v -> v.serialize(2) + ": " + value.get(v).serialize(2)).collect(Collectors.joining("\n")) + "\n}", indent));
    }

    @Override
    public String serializeDocument() {
        return DmlSerializer.serializeComment(comment(), 0) + DmlSerializer.indent(value.keySet().stream().map(v -> v.serialize(0) + ": " + value.get(v).serialize(0)).collect(Collectors.joining("\n")), 0);
    }

    @Override
    public String toString() {
        return "DmlObject{" + value + "}";
    }
}
