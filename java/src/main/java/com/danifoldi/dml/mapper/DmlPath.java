package com.danifoldi.dml.mapper;

@Retention(RetentionPolicy.RUNTIME)
public @interface DmlPath {
    public String path() default null;
}