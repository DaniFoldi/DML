package com.danifoldi.dml;

import com.danifoldi.dml.type.DmlComment;
import com.danifoldi.dml.type.DmlDocument;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class DmlSerializer {

    public static String indent(String text, int spaces) {
        return Arrays.stream(text.split("\n")).map(l -> " ".repeat(spaces) + l).collect(Collectors.joining("\n"));
    }

    public static String serializeComment(DmlComment comment, int indent) {
        if (comment == null || comment.value().isEmpty()) {
            return "";
        }

        return comment.serialize(indent) + "\n";
    }

    public static String serialize(DmlDocument document) {
        return document.serializeDocument();
    }

    public static void serialize(DmlDocument document, Path file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write(serialize(document));
        }
    }
}
