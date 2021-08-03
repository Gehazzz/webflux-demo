package com.example.minion.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Word {
    private String word;
    private String translation;
}
