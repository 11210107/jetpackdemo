package com.wz.jetpackdemo.annotation;

public class AnnotationTest {

    @MyAnnotation1
    public void empty(){
        System.out.println("\nempty");
    }

    @MyAnnotation1({"girl", "boy"})
    public void somebody(String name, int age) {
        System.out.println("\nsomebody: " + name + ", " + age);
    }

}
