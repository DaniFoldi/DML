package com.danifoldi.dml.mapper;

@Retention(RetentionPolicy.RUNTIME)
public @interface DmlDefault {
    public String value() default null;
}