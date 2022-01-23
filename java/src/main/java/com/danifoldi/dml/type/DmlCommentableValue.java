package com.danifoldi.dml.type;

public abstract class DmlCommentableValue extends DmlValue {
    private DmlComment comment;

    public void comment(DmlComment comment) {
        this.comment = comment;
    }

    public DmlComment comment() {
        return this.comment;
    }

    public DmlCommentableValue withComment(String comment) {
        this.comment = new DmlComment(comment);
        return this;
    }
}
