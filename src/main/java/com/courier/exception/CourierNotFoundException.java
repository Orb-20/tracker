package com.courier.exception;

public class CourierNotFoundException extends RuntimeException {

    public CourierNotFoundException(String fieldName, String fieldValue) {
        super("Courier not found with " + fieldName + ": " + fieldValue);
    }
}
