package com.simplecodec.utils;

/**
 * Simple assertion utility class for validating conditions
 */
public class Assert {
    
    /**
     * Asserts that a condition is true
     * 
     * @param condition The condition to check
     * @throws IllegalArgumentException if the condition is false
     */
    public static void isTrue(boolean condition) {
        isTrue(condition, "Assertion failed");
    }
    
    /**
     * Asserts that a condition is true
     * 
     * @param condition The condition to check
     * @param message The exception message to use if the condition is false
     * @throws IllegalArgumentException if the condition is false
     */
    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * Asserts that an object is not null
     * 
     * @param object The object to check
     * @throws IllegalArgumentException if the object is null
     */
    public static void notNull(Object object) {
        notNull(object, "Object must not be null");
    }
    
    /**
     * Asserts that an object is not null
     * 
     * @param object The object to check
     * @param message The exception message to use if the object is null
     * @throws IllegalArgumentException if the object is null
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
