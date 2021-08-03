package com.example.minion.service;

import com.example.minion.model.Grimace;
import com.example.minion.model.Word;

import java.util.List;

public interface WordsService {
    Word translate(String word);
    List<Grimace> rhymeGrimace(String word, int limit);
}
