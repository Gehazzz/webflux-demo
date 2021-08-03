package com.example.minionreactive.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

@Data
@Builder
public class Word {
    @Size(min = 2, max = 10, message = "word must be between 1 and 20 characters")
    private String word;
    @Size(min = 2, max = 10, message = "translation must be between 1 and 20 characters")
    private String translation;
}
