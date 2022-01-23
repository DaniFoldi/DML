package com.danifoldi.dml.type;

import com.danifoldi.dml.exception.DmlTypeException;

public abstract class DmlValue {

    public DmlArray asArray() {

        try {

            return (DmlArray)this;
        } catch (ClassCastException e) {

            throw new DmlTypeException("DmlArray", this.getClass().getTypeName());
        }
    }

    public DmlBoolean asBoolean() {

        try {

            return (DmlBoolean)this;
        } catch (ClassCastException e) {

            throw new DmlTypeException("DmlBoolean", this.getClass().getTypeName());
        }
    }

    public DmlNumber asNumber() {

        try {

            return (DmlNumber)this;
        } catch (ClassCastException e) {

            throw new DmlTypeException("DmlNumber", this.getClass().getTypeName());
        }
    }

    public DmlObject asObject() {

        try {

            return (DmlObject)this;
        } catch (ClassCastException e) {

            throw new DmlTypeException("DmlObject", this.getClass().getTypeName());
        }
    }

    public DmlString asString() {

        try {

            return (DmlString)this;
        } catch (ClassCastException e) {

            throw new DmlTypeException("DString", this.getClass().getTypeName());
        }
    }

    public abstract String serialize(int indent);
}
