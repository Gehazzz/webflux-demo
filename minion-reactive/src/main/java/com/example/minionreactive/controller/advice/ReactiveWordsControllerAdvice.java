package com.example.minionreactive.controller.advice;

import com.example.minionreactive.exception.TranslationDoesNotExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ReactiveWordsControllerAdvice {
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onConstraintValidationException(
            WebExchangeBindException exception) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        exception
                .getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> error.getViolations().add(
                        new Violation(fieldError.getField(), fieldError.getDefaultMessage())
                ));
        return error;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            error.getViolations().add(
                    new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return error;
    }

    @ExceptionHandler(TranslationDoesNotExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public InternalErrorResponse onTranslationDoesNotExists(TranslationDoesNotExistsException e) {
        return new InternalErrorResponse(TranslationDoesNotExistsException.getErrorCode(), e.getMessage());
    }
}
