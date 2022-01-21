package com.danifoldi.dml.mapper;

@Retention(RetentionPolicy.RUNTIME)
public @interface DmlComment {
    public String value() default null;
}