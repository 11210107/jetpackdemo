package com.wz.jetpackdemo.annotation;

import junit.framework.TestCase;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationTestTest extends TestCase {
    @Test
    public void testAnnotationValue() {
        AnnotationTest annotationTest = new AnnotationTest();
        Class<AnnotationTest> annotationTestClass = AnnotationTest.class;
        try {
            Method somebody = annotationTestClass.getMethod("somebody", new Class[]{String.class, int.class});
            somebody.invoke(annotationTest, new Object[]{"lily", 18});
            iteratorAnnotations(somebody);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Test
    private void iteratorAnnotations(Method method) {
        if (method.isAnnotationPresent(MyAnnotation1.class)) {
            MyAnnotation1 annotation = method.getAnnotation(MyAnnotation1.class);
            String[] values = annotation.value();
            for (String value : values) {
                System.out.println("str: " + value);
            }
        }
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }
    }
}