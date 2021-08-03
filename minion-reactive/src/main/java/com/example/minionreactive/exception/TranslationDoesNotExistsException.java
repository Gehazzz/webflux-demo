package com.example.minionreactive.exception;

public class TranslationDoesNotExistsException extends RuntimeException{
    private static final String message = "translation for word %s doesn't exists";
    private static final int errorCode = 101;

    public TranslationDoesNotExistsException(String word) {
        super(String.format(message, word));
    }

    public static int getErrorCode() {
        return errorCode;
    }
}
