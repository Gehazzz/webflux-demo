package com.example.minionreactive.controller;

import com.example.minionreactive.model.Grimace;
import com.example.minionreactive.model.Word;
import com.example.minionreactive.service.ReactiveWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("words")
public class ReactiveWordsController {
    @Autowired
    private ReactiveWordsService wordsService;

    @GetMapping("translate/{word}")
    public Mono<Word> translate(@PathVariable String word) {
        return wordsService.translate(word);
    }

    //Don't do this, if you call block this will be not reactive any more!!!
    @GetMapping("translate-wrong-usage/{word}")
    public Word translateWrongUsage(@PathVariable String word) {
        return wordsService.translate(word).block();
    }

    /**
     * line - 187
     *
     * @see org.springframework.http.codec.json.AbstractJackson2Encoder
     */

    @GetMapping("rhymes/{word}")
    public Flux<Grimace> rhymes(@PathVariable String word, @RequestParam(required = false, defaultValue = "10") int limit) {
        return wordsService.rhymeGrimace(word, limit);
    }

    @GetMapping(value = "stream/rhymes/{word}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Grimace> streamRhymes(@PathVariable String word, @RequestParam(required = false, defaultValue = "10") int limit) {
        return wordsService.rhymeGrimace(word, limit);
    }

    @PostMapping
    public Mono<Word> addWord(@RequestBody Mono<Word> word, @RequestHeader Map<String, String> headers) {
        System.out.println(headers);
        return wordsService.addWord(word);
    }
}
