package com.example.minionreactive.service;

import com.example.minionreactive.model.Grimace;
import com.example.minionreactive.model.Word;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveWordsService {
    Mono<Word> translate(String word);
    Mono<Word> translateWithException(String word);
    Flux<Grimace> rhymeGrimace(String word, int limit);
    Mono<Word> addWord(Mono<Word> newWord);
}
