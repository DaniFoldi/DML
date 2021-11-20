package com.danifoldi.dml.type;

import com.danifoldi.dml.utils.SerializeUtil;

public class DmlComment extends DmlValue {
    private String value;

    public DmlComment(String value) {
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
        int spaceCount = value.lines().anyMatch(s -> s.startsWith("* ")) ? 1 : 2;
        return SerializeUtil.indent(!value.contains("\n") ? "//" + (value.startsWith(" ") ? "" : " ") + value : "/* " + SerializeUtil.indent(value, spaceCount) + "\n */", indent);
    }

    @Override
    public String toString() {
        return "DmlComment{" + value + "}";
    }
}
