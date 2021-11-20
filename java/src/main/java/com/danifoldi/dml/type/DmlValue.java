package com.danifoldi.dml.type;

public abstract class DmlValue {

    public DmlArray asArray() {
        return (DmlArray)this;
    }

    public DmlBoolean asBoolean() {
        return (DmlBoolean)this;
    }

    public DmlNumber asNumber() {
        return (DmlNumber)this;
    }

    public DmlObject asObject() {
        return (DmlObject)this;
    }

    public DmlString asString() {
        return (DmlString)this;
    }

    public abstract String serialize(int indent);
}
