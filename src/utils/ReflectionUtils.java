package utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static void inspectClass(Class<?> clazz) {
        System.out.println("Class: " + clazz.getSimpleName());

        System.out.println("Fields:");
        for (Field f : clazz.getDeclaredFields()) {
            System.out.println(" - " + f.getName());
        }

        System.out.println("Methods:");
        for (Method m : clazz.getDeclaredMethods()) {
            System.out.println(" - " + m.getName());
        }
    }
}