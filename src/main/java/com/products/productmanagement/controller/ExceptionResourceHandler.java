package com.products.productmanagement.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.products.productmanagement.exception.*;
import lombok.Data;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@ControllerAdvice
public class ExceptionResourceHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error unexpectedException(Exception exception) {
        return createErrorMessage("Unexpected error processing the request");
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error productNotFoundException(ProductNotFoundException exception) {
        return createErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(ProductNameAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error productNameAlreadyExistException(ProductNameAlreadyExistException exception) {
        return createErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(DuplicatedProductNameOnDatabaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error duplicatedProductNameOnDatabaseException(DuplicatedProductNameOnDatabaseException exception) {
        return createErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(SaveProductException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error saveProductException(SaveProductException exception) {
        return createErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(DeleteProductException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error deleteProductException(DeleteProductException exception) {
        return createErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(UpdateProductException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error updateProductException(UpdateProductException exception) {
        return createErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error imageNotFoundException(ImageNotFoundException exception) {
        return createErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(SaveImageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error saveImageException(SaveImageException exception) {
        return createErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(DeleteImageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error deleteImageException(DeleteImageException exception) {
        return createErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(UpdateImageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error updateImageException(UpdateImageException exception) {
        return createErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(DuplicatedImageTypeOnDatabaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error duplicatedImageTypeOnDatabaseException(DuplicatedImageTypeOnDatabaseException exception) {
        return createErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(ImageTypeAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error imageTypeAlreadyExistException(ImageTypeAlreadyExistException exception) {
        return createErrorMessage(exception.getMessage());
    }

    @Data
    public static class Error {

        private final ConfirmMessage confirmMessage;

        @Data
        private static class ConfirmMessage {

            private final RequestStatusCode requestStatusCode;

            @JsonProperty("requestID")
            private final RequestId requestId;

            private final List<ProcessMessage> processMessages;
        }

        @Data
        private static class RequestStatusCode {

            private final String codeValue;
        }

        @Data
        private static class RequestId {

            private final String idValue;
        }

        @Data
        private static class ProcessMessage {

            private final MessageTypeCode messageTypeCode;

            private final UserMessage userMessage;
        }

        @Data
        private static class MessageTypeCode {
            private final String codeValue;
        }

        @Data
        private static class UserMessage {
            private final String messageTxt;
        }
    }

    private Error createErrorMessage(String error) {
        return createErrorMessage(Arrays.asList(error));
    }

    private Error createErrorMessage(List<String> errorList) {
        List<Error.ProcessMessage> processMessages = new LinkedList<>();
        Error.RequestStatusCode requestStatusCode = new Error.RequestStatusCode("failed");
        for (String error : errorList) {
            Error.UserMessage userMessage = new Error.UserMessage(error);
            Error.MessageTypeCode messageTypeCode = new Error.MessageTypeCode("error");
            processMessages.add(new Error.ProcessMessage(messageTypeCode, userMessage));
        }
        return new Error(new Error.ConfirmMessage(requestStatusCode, new ExceptionResourceHandler.Error.RequestId(String.valueOf(MDC.get("requestId"))), processMessages));
    }

}
