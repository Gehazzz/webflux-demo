package com.example.minionreactive.router.handler;

import com.example.minionreactive.model.Grimace;
import com.example.minionreactive.model.Word;
import com.example.minionreactive.service.ReactiveWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class WordsRequestHandler {
    @Autowired
    private ReactiveWordsService wordsService;

    public Mono<ServerResponse> translateHandler(ServerRequest serverRequest) {
        String word = serverRequest.pathVariable("word");
        Mono<Word> translated = wordsService.translate(word);
        return ServerResponse.ok().body(translated, Word.class);
    }

    public Mono<ServerResponse> rhymesHandler(ServerRequest serverRequest) {
        String word = serverRequest.pathVariable("word");
        int limit = serverRequest.queryParam("limit").map(Integer::parseInt).orElse(10);
        Flux<Grimace> grimaceFlux = wordsService.rhymeGrimace(word, limit);
        return ServerResponse.ok().body(grimaceFlux, Grimace.class);
    }

    public Mono<ServerResponse> rhymesStreamHandler(ServerRequest serverRequest) {
        String word = serverRequest.pathVariable("word");
        int limit = serverRequest.queryParam("limit").map(Integer::parseInt).orElse(10);
        Flux<Grimace> grimaceFlux = wordsService.rhymeGrimace(word, limit);
        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(grimaceFlux, Grimace.class);
    }

    public Mono<ServerResponse> addWordHandler(ServerRequest serverRequest) {
        Mono<Word> wordMono = serverRequest.bodyToMono(Word.class);
        Mono<Word> wordMonoResponse = wordsService.addWord(wordMono);
        return ServerResponse.ok().body(wordMonoResponse, Word.class);
    }
}
