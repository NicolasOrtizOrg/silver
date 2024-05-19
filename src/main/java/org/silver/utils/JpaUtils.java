package org.silver.utils;

import java.lang.reflect.Field;

public class JpaUtils {

    private JpaUtils(){}

    public static void copyNonNullProperties(Object source, Object target) {
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(source);
                if (value != null) {
                    Field targetField = target.getClass().getDeclaredField(field.getName());
                    targetField.setAccessible(true);
                    targetField.set(target, value);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
