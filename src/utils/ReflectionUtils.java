package utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

public class ReflectionUtils {

    public static void inspectClass(Class<?> clazz) {
        System.out.println("\n=== Reflection Inspection ===");
        System.out.println("Class: " + clazz.getName());

        System.out.println("\nFields:");
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            System.out.println("  " + Modifier.toString(f.getModifiers()) +
                    " " + f.getType().getSimpleName() + " " + f.getName());
        }

        System.out.println("\nMethods:");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println("  " + m.getName() + " returns " + m.getReturnType().getSimpleName());
        }

        System.out.println("\nSuperclass: " + clazz.getSuperclass().getName());

        System.out.println("\nInterfaces:");
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> i : interfaces) {
            System.out.println("  " + i.getSimpleName());
        }

        System.out.println("\nAnnotations:");
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation a : annotations) {
            System.out.println("  " + a.annotationType().getSimpleName());
        }
    }
}