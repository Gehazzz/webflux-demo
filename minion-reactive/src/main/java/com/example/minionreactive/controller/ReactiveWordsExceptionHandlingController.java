package com.example.minionreactive.controller;

import com.example.minionreactive.model.Word;
import com.example.minionreactive.service.ReactiveWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Validated
@RestController
@RequestMapping("words-exception-handling")
public class ReactiveWordsExceptionHandlingController {
    @Autowired
    private ReactiveWordsService wordsService;

    @GetMapping("translate/{word}")
    public Mono<Word> translate(@PathVariable @Size(min = 2, max = 20, message = "word must be between 1 and 20 characters") String word) {
        //return wordsService.translate(word);
        return wordsService.translateWithException(word);
    }

    @PostMapping
    public Mono<Word> addWord(@Valid @RequestBody Mono<Word> word) {
        return wordsService.addWord(word);
    }
}
