/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com;

public class App {
    public String getGreeting() {
        return "Helloworld";
    }

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(new App().getGreeting());
                Class c1 = Class.forName("java.lang.Boolean");
 
        System.out.println("Class represented by c1: "
                         + c1.toString());

    }
}