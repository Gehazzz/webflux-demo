package com.example.minionreactive.controller.advice;

import lombok.Data;

@Data
public class Violation {
    private final String fieldName;
    private final String message;
}
