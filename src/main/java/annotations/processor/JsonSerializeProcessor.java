package annotations.processor;

import annotations.JsonAttribute;
import annotations.exception.JsonSerializeException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public class JsonSerializeProcessor {

    public static String converToJson(Object object) {
        if (Objects.isNull(object)) {
            throw new JsonSerializeException("The object to serialize can not be null");
        }

        Field[] attributes = object.getClass().getDeclaredFields();

        return Arrays.stream(attributes)
                .filter(f -> f.isAnnotationPresent(JsonAttribute.class))
                .map( f -> {
                    f.setAccessible(true);
                    String name = f.getAnnotation(JsonAttribute.class).name().equals("") ?
                            f.getName() : f.getAnnotation(JsonAttribute.class).name();

                    try {
                        Object value = f.get(object);
                        if (value instanceof String) {
                            String newValue = (String) value;
                            f.set(object, newValue);
                        }
                        return "\"" + name + "\":\"" + f.get(object) + "\"";
                    } catch (IllegalAccessException e) {
                        throw new JsonSerializeException("Error to serialize json: " + e.getMessage());
                    }
                })
                .reduce("{", (a, b) -> {
                    if ("{".equals(a)) {
                        return a + b;
                    }
                    return a + ", " + b;
                })
                .concat("}");
    }

}
