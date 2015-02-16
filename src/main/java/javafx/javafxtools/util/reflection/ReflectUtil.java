/*
 * The MIT License
 *
 * Copyright 2015 Thaylon Guedes Santos.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package javafx.javafxtools.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Thaylon Guedes Santos
 * @email thaylon_guedes@hotmail.com
 */
public class ReflectUtil {

    public static List<Field> getFieldContainsAnnotations(Class target, Class annotation) {
        List<Field> fieldsDaClasse = getFields(target);
        return (List<Field>) fieldsDaClasse.stream().filter((Field field) -> field.isAnnotationPresent(annotation)).collect(Collectors.toList());
    }

    public static List<Field> getFieldNotContainsAnnotations(Class target, Class annotation) {
        List<Field> fieldsDaClasse = getFields(target);
        return (List<Field>) fieldsDaClasse.stream().filter((Field field) -> !field.isAnnotationPresent(annotation)).collect(Collectors.toList());
    }

    public static List<Method> getMethodContainsAnnotations(Class target, Class annotation) {
        List<Method> fieldsDaClasse = getMethods(target);
        return (List<Method>) fieldsDaClasse.stream().filter((Method method) -> method.isAnnotationPresent(annotation)).collect(Collectors.toList());
    }

    public static List<Method> getMethodNotContainsAnnotations(Class target, Class annotation) {
        List<Method> fieldsDaClasse = getMethods(target);
        return (List<Method>) fieldsDaClasse.stream().filter((Method method) -> !method.isAnnotationPresent(annotation)).collect(Collectors.toList());
    }

    public static List<Field> getFields(Class target) {
        return Arrays.asList(target.getDeclaredFields());
    }

    public static List<Method> getMethods(Class target) {
        return Arrays.asList(target.getDeclaredMethods());
    }

    public static String getValueToString(Object target, Field campo) {
        Object o = getValue(target, campo);
        return o != null ? o.toString() : "";
    }

    public static String getValueToString(Object target, Method method) {
        Object o = getValue(target, method);
        return o != null ? o.toString() : "";
    }

    public static Object getValue(Object target, Field campo) {
        campo.setAccessible(true);
        try {
            Object o = campo.get(target);
            return o;
        } catch (IllegalAccessException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return null;
    }

    public static Object getValue(Object target, Method method) {
        method.setAccessible(true);
        try {
            return method.invoke(target);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return null;
    }

}
