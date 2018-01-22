package com.products.productmanagement.exception;

public class DuplicatedProductNameOnDatabaseException extends RuntimeException {
    public DuplicatedProductNameOnDatabaseException(String s) {
        super(s);
    }
}
