package com.example.minion.controller;

import com.example.minion.model.Grimace;
import com.example.minion.model.Word;
import com.example.minion.service.WordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("words")
public class WordsController {
    @Autowired
    private WordsService wordsService;

    @GetMapping("translate/{word}")
    public Word translate(@PathVariable String word){
        return wordsService.translate(word);
    }
    @GetMapping("rhymes/{word}")
    public List<Grimace> rhymes(@PathVariable String word, @RequestParam(required = false) int limit){
        return wordsService.rhymeGrimace(word, limit);
    }
}
