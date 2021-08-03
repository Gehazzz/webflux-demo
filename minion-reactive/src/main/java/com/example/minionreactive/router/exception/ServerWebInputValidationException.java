package com.example.minionreactive.router.exception;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ServerWebInputException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServerWebInputValidationException extends ServerWebInputException {
    private final Map<String, String> errors;

    public ServerWebInputValidationException(Errors errors) {
        super("Validation failure");
        this.errors = errors.getFieldErrors()
                .stream()
                .collect(
                        Collectors.toMap(
                                FieldError::getField,
                                e -> Optional.ofNullable(e.getDefaultMessage()).orElse("")
                        )
                );
    }

    public ServerWebInputValidationException(String field, String message) {
        super("Validation failure");
        this.errors = new HashMap<>() {{
            put(field, message);
        }};
    }

    public ServerWebInputValidationException(Map<String, String> errors) {
        super("Validation failure");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
