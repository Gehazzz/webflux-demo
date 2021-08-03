package com.example.minionreactive.service;

import com.example.minionreactive.exception.TranslationDoesNotExistsException;
import com.example.minionreactive.model.Grimace;
import com.example.minionreactive.model.Word;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReactiveWordsServiceImpl implements ReactiveWordsService {

    private static final Map<String, String> dictionary = new ConcurrentHashMap<>() {{
        put("hello", "bello");
        put("goodbye", "poopaye");
        put("thank you", "bank yu");
        put("thanks", "bank yu");
        put("i'm hungry", "me want banana");
        put("hungry", "want banana");
        put("underwear", "i swear");
        put("fire", "bee do");
        put("i hate you", "tatata bala tu");
        put("hate", "bala");
        put("toy", "baboi");
        put("what", "po ka");
        put("apple", "bable");
        put("ice cream", "gelato");
        put("butt", "butt");
        put("one", "nah");
        put("two", "dul");
        put("three", "sae");
        put("for you", "para tu");
        put("you", "tu");
        put("chair", "chasy");
        put("cheers", "kan pai");
        put("can we start", "pwede na");
        put("look at you", "luk at tu");
        put("kiss", "muak muak");
        put("i'm sorry", "ditto");
        put("bottom", "bottom");
        put("stop", "stopa");
    }};

    private final Random random = new Random();

    @Override
    public Mono<Word> translate(String word) {
        return Mono.fromSupplier(() -> Word.builder()
                .word(word)
                .translation(dictionary.getOrDefault(word, "banana"))
                .build());
    }

    @Override
    public Mono<Word> translateWithException(String word) {
        return Mono.just(word).handle(((toTranslate, sink) -> {
            Optional.ofNullable(dictionary.get(toTranslate)).map(translation -> Word.builder()
                    .word(word)
                    .translation(translation)
                    .build())
                    .ifPresentOrElse(
                            sink::next,
                            () -> sink.error(new TranslationDoesNotExistsException(word))
                    );
        }));
    }

    @Override
    public Flux<Grimace> rhymeGrimace(String word, int limit) {
        return Flux.range(1, limit)
                //.delayElements(Duration.ofSeconds(1)) // <- non blocking, will stop calculation right after stopping request
                .doOnNext(i -> Sleep.sleepSeconds(1)) // <- blocking, will calculate couple values after stopping request
                .doOnNext(i -> System.out.println("reactive-words-service processing : " + i))
                .map(i -> Grimace.builder().grimace(grimaceGenerator(word)).number(i).build());
    }

    @Override
    public Mono<Word> addWord(Mono<Word> newWord) {
        return newWord
                .doOnNext(word -> dictionary.put(word.getWord(), word.getTranslation()));
    }

    //NOT REACTIVE!!!!
   /* @Override
    public Flux<Grimace> rhymeGrimace(String word, int limit) {
        List<Grimace> grimaces = IntStream.rangeClosed(1, limit)
                .peek(i -> Sleep.sleepSeconds(1))
                .peek(i -> System.out.println("reactive-words-service processing : " + i))
                .mapToObj(i -> Grimace.builder().grimace(grimaceGenerator(word)).number(i).build())
                .collect(Collectors.toList());
        return Flux.fromIterable(grimaces);
    }*/

    private String grimaceGenerator(String word) {
        List<String> minionGrimaces = new ArrayList<>(dictionary.values());
        int numberOfWordsInGrimace = random.nextInt(2) + 1;
        int putWordAfter = random.nextInt(numberOfWordsInGrimace);
        StringBuilder grimace = new StringBuilder();
        for (int i = 0; i <= numberOfWordsInGrimace; i++) {
            if (i != 0)
                grimace.append(" ");
            int randomIndex = random.nextInt(minionGrimaces.size());
            grimace.append(minionGrimaces.get(randomIndex));
            if (putWordAfter == i) {
                grimace.append(" ").append(word);
            }
        }
        return grimace.toString();
    }
}
