package com.danifoldi.dml.utils;

import com.danifoldi.dml.type.DmlComment;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SerializeUtil {

    public static String indent(String text, int spaces) {
        return Arrays.stream(text.split("\n")).map(l -> " ".repeat(spaces) + l).collect(Collectors.joining("\n"));
    }

    public static String serializeComment(DmlComment comment, int indent) {
        if (comment == null || comment.value().isEmpty()) {
            return "";
        }

        return comment.serialize(indent) + "\n";
    }
}
