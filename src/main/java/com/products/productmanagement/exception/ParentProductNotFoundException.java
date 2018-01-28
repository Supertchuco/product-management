package com.products.productmanagement.exception;

public class ParentProductNotFoundException extends RuntimeException {
    public ParentProductNotFoundException(String s) {
        super(s);
    }
}