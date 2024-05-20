package org.silver.utils;

import java.lang.reflect.Field;

public class JpaUtils {

    private JpaUtils(){}

    /**
     * Copia los atributos no nulos de una clase a la otra.
     * Sirve para mapear/copiar los atributos no nulos de un DTO a un Entity.
     * Sirve para generar consultas UPDATE dinámicas en la base de datos.
     * */
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
                //
                // throw new RuntimeException(e);
            }
        }
    }

}
