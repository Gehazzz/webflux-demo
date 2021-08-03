package com.example.minionreactive.controller.advice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InternalErrorResponse {
    private int errorCode;
    private String message;
}
