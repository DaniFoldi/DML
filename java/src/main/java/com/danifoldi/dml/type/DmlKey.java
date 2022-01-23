package com.danifoldi.dml.type;

import com.danifoldi.dml.DmlSerializer;

import java.util.Objects;

public class DmlKey extends DmlCommentableValue implements Comparable<DmlKey> {
    private String value;

    public DmlKey(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public void value(String value) {
        this.value = value;
    }

    @Override
    public String serialize(int indent) {
        return DmlSerializer.serializeComment(comment(), indent) + DmlSerializer.indent(value, indent);
    }

    @Override
    public String toString() {
        return "DmlKey{" + value + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DmlKey dmlKey = (DmlKey) o;
        return Objects.equals(value, dmlKey.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(DmlKey o) {
        return value.compareTo(o.value);
    }
}
