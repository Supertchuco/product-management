package com.products.productmanagement.exception;

public class ProductNameAlreadyExistException extends RuntimeException {
    public ProductNameAlreadyExistException(String s) {
        super(s);
    }
}