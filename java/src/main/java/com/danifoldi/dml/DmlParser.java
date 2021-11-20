package com.danifoldi.dml;

import com.danifoldi.dml.type.DmlArray;
import com.danifoldi.dml.type.DmlBoolean;
import com.danifoldi.dml.type.DmlComment;
import com.danifoldi.dml.type.DmlKey;
import com.danifoldi.dml.type.DmlNumber;
import com.danifoldi.dml.type.DmlObject;
import com.danifoldi.dml.type.DmlDocument;
import com.danifoldi.dml.type.DmlString;
import com.danifoldi.dml.type.DmlValue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class DmlParser {
    private final String dmlString;
    private int pointer;
    private int line;
    private int column;

    private DmlParser(String dmlString) {
        this.dmlString = dmlString + "\n";
        line = 1;
        column = 1;
        pointer = 0;
    }

    private void step() {
        if (currentChar() == '\n') {
            column = 0;
            line++;
        }

        column++;
        pointer++;
    }

    private void step(int count) {
        for (int i = 0; i < count; i++) {
            step();
        }
    }

    private void stepNextNonWhitespace() {
        while (pointer < dmlString.length() && isWhitespace(currentChar())) {
            step();
        }
    }

    private int nextNonWhitespace(int pointer) {
        while (pointer < dmlString.length() && isWhitespace(charAt(pointer))) {
            pointer++;
        }

        return pointer;
    }

    private char nextNonWhitespaceChar(int pointer) {
        while (pointer < dmlString.length() && isWhitespace(charAt(pointer))) {
            pointer++;
        }

        return charAt(pointer);
    }

    private char charAt(int pointer) {
        return dmlString.charAt(pointer);
    }

    private char currentChar() {
        return charAt(pointer);
    }

    private char nextChar() {
        return charAt(pointer + 1);
    }

    private boolean isCommentStart(char c, char n) {
        return c == '/' && (n == '/' || n == '*');
    }

    private boolean isMultilineCommentEnd(char c, char n) {
        return c == '*' && n == '/';
    }

    private boolean isWhitespace(char c) {
        return String.valueOf(c).matches("\\s");
    }

    private boolean isDigit(char c) {
        return String.valueOf(c).matches("[0-9]");
    }

    private boolean isNewline(char c) {
        return c == '\n';
    }

    private boolean isString(char c) {
        return c == '"' || c == '\'';
    }

    private boolean isKey(char c) {
        return String.valueOf(c).matches("[a-zA-Z0-9-_]");
    }


    private DmlComment parseComment() throws DmlParseException {
        StringBuilder comment = new StringBuilder();

        stepNextNonWhitespace();

        while (isCommentStart(currentChar(), nextChar())) {
            switch (nextChar()) {
                case '/' -> {
                    step();
                    step();
                    int start = pointer;
                    while (!isNewline(currentChar())) {
                        step();
                    }
                    comment.append(dmlString, start, pointer);
                }
                case '*' -> {
                    step();
                    step();
                    int start = pointer;
                    while (!isMultilineCommentEnd(currentChar(), nextChar())) {
                        step();
                    }
                    comment.append(dmlString, start, pointer);
                    step();
                    step();
                }
            }
        }

        return new DmlComment(comment.toString());
    }

    private DmlBoolean parseBoolean(DmlComment comment) throws DmlParseException {
        if (dmlString.substring(pointer, pointer + 4).equalsIgnoreCase("true")) {
            step(4);
            DmlBoolean bool = new DmlBoolean(true);
            bool.comment(comment);
            return bool;
        }
        if (dmlString.substring(pointer, pointer + 5).equalsIgnoreCase("false")) {
            step(5);
            DmlBoolean bool = new DmlBoolean(false);
            bool.comment(comment);
            return bool;
        }
        throw new DmlParseException(line, column, "true or false", dmlString.substring(pointer, pointer + 4));
    }

    private DmlNumber parseNumber(DmlComment comment) throws DmlParseException {
        stepNextNonWhitespace();
        int n = pointer;
        boolean negative = currentChar() == '-';
        if (negative) {
            step();
        }
        String whole;
        String fraction = "0";
        String exponent = "0";

        int start = pointer;
        while (isDigit(currentChar())) {
            step();
        }
        whole = pointer > start ? dmlString.substring(start, pointer) : "0";
        if (currentChar() == '.') {
            step();
            start = pointer;
            while (isDigit(currentChar())) {
                step();
            }
            fraction = pointer > start ? dmlString.substring(start, pointer) : "0";
        }
        if (currentChar() == 'e') {
            step();
            start = pointer;
            while (isDigit(currentChar())) {
                step();
            }
            boolean nexp = currentChar() == '-';
            if (nexp) {
                step();
            }
            exponent = (nexp ? "-" : "") + (pointer > start ? dmlString.substring(start, pointer) : "0");
        }
        BigDecimal b = new BigDecimal(whole + "." + fraction);
        if (negative) {
            b = b.negate();
        }
        b = b.scaleByPowerOfTen(Integer.parseInt(exponent));
        DmlNumber number = new DmlNumber(b, dmlString.substring(n, pointer));
        number.comment(comment);
        return number;
    }

    private DmlString parseString(DmlComment comment) throws DmlParseException {
        stepNextNonWhitespace();
        switch (charAt(pointer)) {
            case '\'' -> {
                int start = pointer;
                boolean didEscape;
                do {
                    didEscape = currentChar() == '\\';
                    step();
                } while (currentChar() != '\'' || didEscape);
                step();
                DmlString string = new DmlString(dmlString.substring(start + 1, pointer - 1).replace("\\'", "'"));
                string.comment(comment);
                return string;
            }
            case '"' -> {
                int start = pointer;
                boolean didEscape;
                do {
                    didEscape = currentChar() == '\\';
                    step();
                } while (currentChar() != '"' || didEscape);
                step();
                DmlString string = new DmlString(dmlString.substring(start + 1, pointer - 1).replace("\\\"", "\""));
                string.comment(comment);
                return string;
            }
            default -> throw new DmlParseException(line, column, "' or \"", String.valueOf(charAt(pointer)));
        }
    }

    private DmlKey parseKey(DmlComment comment) throws DmlParseException {
        stepNextNonWhitespace();
        int start = pointer;
        while (isKey(charAt(pointer))) {
            step();
        }
        DmlKey key = new DmlKey(dmlString.substring(start, pointer));
        key.comment(comment);
        return key;
    }

    private DmlValue parseValue(DmlComment comment) throws DmlParseException {
        stepNextNonWhitespace();
        return switch (charAt(pointer)) {
            case '{' -> parseObject(comment);
            case '[' -> parseArray(comment);
            case '\'', '"' -> parseString(comment);
            case 't', 'f' -> parseBoolean(comment);
            default -> parseNumber(comment);
        };
    }

    private DmlObject parseObjectValue(boolean toplevel) throws DmlParseException {
        DmlObject object = new DmlObject(new HashMap<>());
        while (charAt(nextNonWhitespace(pointer)) != '}' && pointer < dmlString.length()) {
            stepNextNonWhitespace();
            DmlComment comment = parseComment();
            stepNextNonWhitespace();
            DmlKey key = parseKey(comment);
            stepNextNonWhitespace();
            if (currentChar() != ':') {
                throw new DmlParseException(line, column, ":", String.valueOf(currentChar()));
            }
            step();
            stepNextNonWhitespace();
            DmlComment comment1 = parseComment();
            stepNextNonWhitespace();
            DmlValue value = parseValue(comment1);
            object.set(key, value);
            int sline = line;
            stepNextNonWhitespace();
            if ((pointer >= dmlString.length() && toplevel) || currentChar() == '}') {
                break;
            }
            if (currentChar() != ',' && sline == line) {
                throw new DmlParseException(line, column, ",", String.valueOf(currentChar()));
            }
            if (currentChar() == ',') {
                step();
            }
        }
        return object;
    }

    private DmlObject parseObject(DmlComment comment) throws DmlParseException {
        step();
        if (comment == null) {
            comment = parseComment();
        }
        DmlObject object = parseObjectValue(false);
        object.comment(comment);

        if (charAt(nextNonWhitespace(pointer)) != '}') {
            throw new DmlParseException(line, column, "}", String.valueOf(charAt(nextNonWhitespace(pointer))));
        }
        stepNextNonWhitespace();
        step();
        return object;
    }

    private DmlArray parseArray(DmlComment comment) throws DmlParseException {
        step();
        if (comment == null) {
            comment = parseComment();
        }
        DmlArray array = new DmlArray(new ArrayList<>());
        array.comment(comment);

        while (charAt(nextNonWhitespace(pointer)) != ']') {
            stepNextNonWhitespace();
            DmlComment vcomment = parseComment();
            array.add(parseValue(vcomment));
            int sline = line;
            stepNextNonWhitespace();
            if (currentChar() == ']') {
                break;
            }
            if (currentChar() != ',' && sline == line) {
                throw new DmlParseException(line, column, ",", String.valueOf(currentChar()));
            }
            if (currentChar() == ',') {
                step();
            }
        }

        if (charAt(nextNonWhitespace(pointer)) != ']') {
            throw new DmlParseException(line, column, "]", String.valueOf(charAt(nextNonWhitespace(pointer))));
        }
        stepNextNonWhitespace();
        step();
        return array;
    }

    private DmlValue parseDocument() throws DmlParseException {
        stepNextNonWhitespace();
        DmlComment comment = parseComment();
        stepNextNonWhitespace();
        DmlValue result;

        switch (nextNonWhitespaceChar(pointer)) {
            case '{':
                result = parseObject(comment);
                break;
            case '[':
                result = parseArray(comment);
                break;
            default:
                if (isKey(charAt(pointer))) {
                    DmlObject object = parseObjectValue(true);
                    object.comment(comment);
                    result = object;
                } else {
                    throw new DmlParseException(line, column, "{, [, \", ' or key", String.valueOf(charAt(pointer)));
                }
        }

        if (nextNonWhitespace(pointer) < dmlString.length()) {
            throw new DmlParseException(line, column, "document end", String.valueOf(charAt(nextNonWhitespace(pointer))));
        }

        return result;
    }

    public static DmlValue parse(String dmlString) throws DmlParseException {
        return new DmlParser(dmlString).parseDocument();
    }

    public static DmlValue parse(Path file) throws DmlParseException, IOException {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            return parse(reader.lines().collect(Collectors.joining("\n")));
        }
    }

    public static String serialize(DmlDocument document) {
        return document.serializeDocument();
    }

    public static void serialize(DmlDocument document, Path file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write(serialize(document));
        }
    }

    public static void main(String[] args) throws DmlParseException{
        String test = """
                // hello
                a: 1
                /*
                * asd
                */
                b: ["ran\\"d'om",1,2,3,4,5
                6,7,8
                9
                10
                
                ]
                /*
                anyád
                picsája +
                */
                c: {tomi: "van", egysenki: "há mé
                 lenne"}
                 e:[]
                 d:{}
                """;
        DmlObject o = (DmlObject)DmlParser.parse(test);
        System.out.println(o);
        System.out.println(DmlParser.serialize(o));
    }
}



