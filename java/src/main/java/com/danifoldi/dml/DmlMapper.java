package com.danifoldi.dml;

import com.danifoldi.dml.exception.DmlMappingException;
import com.danifoldi.dml.exception.DmlParseException;
import com.danifoldi.dml.mapper.DmlConvertUsing;
import com.danifoldi.dml.mapper.DmlConverter;
import com.danifoldi.dml.mapper.DmlDefault;
import com.danifoldi.dml.mapper.DmlPath;
import com.danifoldi.dml.mapper.builtin.BigDecimalConverter;
import com.danifoldi.dml.mapper.builtin.BigDecimalListConverter;
import com.danifoldi.dml.mapper.builtin.BigDecimalSetConverter;
import com.danifoldi.dml.mapper.builtin.BooleanConverter;
import com.danifoldi.dml.mapper.builtin.BooleanListConverter;
import com.danifoldi.dml.mapper.builtin.BooleanSetConverter;
import com.danifoldi.dml.mapper.builtin.ByteConverter;
import com.danifoldi.dml.mapper.builtin.ByteListConverter;
import com.danifoldi.dml.mapper.builtin.ByteSetConverter;
import com.danifoldi.dml.mapper.builtin.CharacterConverter;
import com.danifoldi.dml.mapper.builtin.CharacterListConverter;
import com.danifoldi.dml.mapper.builtin.CharacterSetConverter;
import com.danifoldi.dml.mapper.builtin.DoubleConverter;
import com.danifoldi.dml.mapper.builtin.DoubleListConverter;
import com.danifoldi.dml.mapper.builtin.DoubleSetConverter;
import com.danifoldi.dml.mapper.builtin.FloatConverter;
import com.danifoldi.dml.mapper.builtin.FloatListConverter;
import com.danifoldi.dml.mapper.builtin.FloatSetConverter;
import com.danifoldi.dml.mapper.builtin.IntegerConverter;
import com.danifoldi.dml.mapper.builtin.IntegerListConverter;
import com.danifoldi.dml.mapper.builtin.IntegerSetConverter;
import com.danifoldi.dml.mapper.builtin.LongConverter;
import com.danifoldi.dml.mapper.builtin.LongListConverter;
import com.danifoldi.dml.mapper.builtin.LongSetConverter;
import com.danifoldi.dml.mapper.builtin.ShortConverter;
import com.danifoldi.dml.mapper.builtin.ShortListConverter;
import com.danifoldi.dml.mapper.builtin.ShortSetConverter;
import com.danifoldi.dml.mapper.builtin.StringConverter;
import com.danifoldi.dml.mapper.builtin.StringListConverter;
import com.danifoldi.dml.mapper.builtin.StringSetConverter;
import com.danifoldi.dml.mapper.builtin.UUIDConverter;
import com.danifoldi.dml.mapper.builtin.UUIDListConverter;
import com.danifoldi.dml.mapper.builtin.UUIDSetConverter;
import com.danifoldi.dml.type.DmlCommentableValue;
import com.danifoldi.dml.type.DmlObject;
import com.danifoldi.dml.type.DmlValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class DmlMapper {

    private static final Map<String, DmlConverter> mapMap = new ConcurrentHashMap<>();

    static {
        class Builtin {
            Boolean tBoolean;
            Collection<Boolean> tBooleanCollection;
            List<Boolean> tBooleanList;
            Set<Boolean> tBooleanSet;

            Byte tByte;
            Collection<Byte> tByteCollection;
            List<Byte> tByteList;
            Set<Byte> tByteSet;

            Character tCharacter;
            Collection<Character> tCharacterCollection;
            List<Character> tCharacterList;
            Set<Character> tCharacterSet;

            Double tDouble;
            Collection<Double> tDoubleCollection;
            List<Double> tDoubleList;
            Set<Double> tDoubleSet;

            Float tFloat;
            Collection<Float> tFloatCollection;
            List<Float> tFloatList;
            Set<Float> tFloatSet;

            Integer tInteger;
            Collection<Integer> tIntegerCollection;
            List<Integer> tIntegerList;
            Set<Integer> tIntegerSet;

            Long tLong;
            Collection<Long> tLongCollection;
            List<Long> tLongList;
            Set<Long> tLongSet;

            Short tShort;
            Collection<Short> tShortCollection;
            List<Short> tShortList;
            Set<Short> tShortSet;

            BigDecimal tBigDecimal;
            Collection<BigDecimal> tBigDecimalCollection;
            List<BigDecimal> tBigDecimalList;
            Set<BigDecimal> tBigDecimalSet;

            String tString;
            Collection<String> tStringCollection;
            List<String> tStringList;
            Set<String> tStringSet;

            UUID tUUID;
            Collection<UUID> tUUIDCollection;
            List<UUID> tUUIDList;
            Set<UUID> tUUIDSet;
        }

        try {
            mapMap.put("boolean", new BooleanConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tBoolean")), new BooleanConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tBooleanCollection")), new BooleanListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tBooleanList")), new BooleanListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tBooleanSet")), new BooleanSetConverter());
            mapMap.put("byte", new ByteConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tByte")), new ByteConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tByteCollection")), new ByteListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tByteList")), new ByteListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tByteSet")), new ByteSetConverter());
            mapMap.put("char", new CharacterConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tCharacter")), new CharacterConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tCharacterCollection")), new CharacterListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tCharacterList")), new CharacterListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tCharacterSet")), new CharacterSetConverter());
            mapMap.put("double", new DoubleConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tDouble")), new DoubleConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tDoubleCollection")), new DoubleListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tDoubleList")), new DoubleListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tDoubleSet")), new DoubleSetConverter());
            mapMap.put("float", new FloatConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tFloat")), new FloatConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tFloatCollection")), new FloatListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tFloatList")), new FloatListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tFloatSet")), new FloatSetConverter());
            mapMap.put("int", new IntegerConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tInteger")), new IntegerConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tIntegerCollection")), new IntegerListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tIntegerList")), new IntegerListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tIntegerSet")), new IntegerSetConverter());
            mapMap.put("long", new LongConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tLong")), new LongConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tLongCollection")), new LongListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tLongList")), new LongListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tLongSet")), new LongSetConverter());
            mapMap.put("short", new ShortConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tShort")), new ShortConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tShortCollection")), new ShortListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tShortList")), new ShortListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tShortSet")), new ShortSetConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tBigDecimal")), new BigDecimalConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tBigDecimalCollection")), new BigDecimalListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tBigDecimalList")), new BigDecimalListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tBigDecimalSet")), new BigDecimalSetConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tString")), new StringConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tStringCollection")), new StringListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tStringList")), new StringListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tStringSet")), new StringSetConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tUUID")), new UUIDConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tUUIDCollection")), new UUIDListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tUUIDList")), new UUIDListConverter());
            mapMap.put(getSignature(Builtin.class.getDeclaredField("tUUIDSet")), new UUIDSetConverter());
        } catch (ReflectiveOperationException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }

    private static String getSignature(Field f) {
        try {
            ParameterizedType type = (ParameterizedType) f.getGenericType();
            return f.getType() + "<" + Arrays.stream(type.getActualTypeArguments()).map(Object::toString).collect(Collectors.joining(",")) + ">";
        } catch (ClassCastException ignored) {
            return f.getType().toString();
        }
    }

    private static DmlConverter findConverter(Field field) {
        try {
            DmlConverter converter;
            DmlConvertUsing convertUsing = field.getAnnotation(DmlConvertUsing.class);
            if (convertUsing == null) {
                converter = mapMap.get(getSignature(field));
            } else {
                converter = convertUsing.converter().getDeclaredConstructor().newInstance();
            }
            if (converter == null) {
                throw new DmlMappingException(getSignature(field));
            }
            return converter;
        } catch (ReflectiveOperationException ignored) {
            throw new DmlMappingException(getSignature(field));
        }
    }

    public static void load(Path file, Object object) throws DmlMappingException, DmlParseException, IOException {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            load(reader.lines().collect(Collectors.joining("\n")), object);
        }
    }

    public static void load(String dmlString, Object object) throws DmlMappingException, DmlParseException {
        load(DmlParser.parse(dmlString).asObject(), object);
    }

    public static void load(DmlObject dmlObject, Object object) throws DmlMappingException {
        try {
            List<Field> fields = Arrays
                    .stream(object.getClass().getDeclaredFields())
                    .peek(Field::trySetAccessible)
                    .filter(f -> f.isAnnotationPresent(DmlPath.class))
                    .toList();

            for (Field field: fields) {
                DmlConverter converter = findConverter(field);
                DmlPath path = field.getAnnotation(DmlPath.class);
                DmlValue o = dmlObject;
                for (String pathComponent: path.value().split("\\.")) {
                    if (o == null) {
                        break;
                    }
                    o = o.asObject().get(pathComponent);
                }

                if (o == null) {
                    DmlDefault defaultValue = field.getAnnotation(DmlDefault.class);
                    field.set(object, converter.fromDefault(defaultValue.value(), defaultValue.comment()));
                } else {
                    field.set(object, converter.convertFrom((DmlCommentableValue)o));
                }
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public static DmlObject snapshot(Object object) {
        DmlObject result = new DmlObject(new HashMap<>());
        update(result, object);
        return result;
    }

    public static DmlObject update(String dmlString, Object object) throws DmlParseException {
        return update(DmlParser.parse(dmlString).asObject(), object);
    }

    public static DmlObject update(DmlObject dmlObject, Object object) {
        DmlObject result = new DmlObject(new HashMap<>());
        try {
            List<Field> fields = Arrays
                    .stream(object.getClass().getDeclaredFields())
                    .peek(Field::trySetAccessible)
                    .filter(f -> f.isAnnotationPresent(DmlPath.class))
                    .toList();

            for (Field field: fields) {
                DmlConverter converter = findConverter(field);
                DmlPath path = field.getAnnotation(DmlPath.class);
                DmlValue o = result;
                List<String> pathComponents = new ArrayList<>(List.of(path.value().split("\\.")));
                String key = pathComponents.remove(pathComponents.size() - 1);
                for (String pathComponent: pathComponents) {
                    if (o.asObject().get(pathComponent) == null) {
                        o.asObject().set(pathComponent, new DmlObject(new HashMap<>()));
                    }
                    o = o.asObject().get(pathComponent);
                }

                DmlDefault defaultValue = field.getAnnotation(DmlDefault.class);
                Object value = field.get(object);
                DmlCommentableValue setValue = (DmlCommentableValue)o.asObject().get(key);
                if (value != null) {
                    if (setValue != null) {
                        o.asObject().set(key, converter.convertTo(value, setValue.comment().value()));
                    } else {
                        o.asObject().set(key, converter.convertTo(value, defaultValue != null ? defaultValue.comment() : ""));
                    }
                } else if (defaultValue != null) {
                    o.asObject().set(key, converter.fromDefault(defaultValue.value(), defaultValue.comment()));
                }

            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return result;
    }
}
