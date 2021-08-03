package com.example.minion.service;

import com.example.minion.model.Grimace;
import com.example.minion.model.Word;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class WordsServiceImpl implements WordsService {

    private static final Map<String, String> dictionary = new HashMap<>() {{
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

    private final List<String> minionGrimaces = new ArrayList<>(dictionary.values());

    private final Random random = new Random();

    @Override
    public Word translate(String word) {
        return Word.builder()
                .word(word)
                .translation(dictionary.getOrDefault(word, "banana"))
                .build();
    }

    @Override
    public List<Grimace> rhymeGrimace(String word, int limit) {
        return IntStream.rangeClosed(0, limit)
                .peek(i -> Sleep.sleepSeconds(1))
                .peek(i -> System.out.println("words-service processing : " + i))
                .mapToObj(i -> Grimace.builder().grimace(grimaceGenerator(word)).id(i).build())
                .collect(Collectors.toList());
    }

    private String grimaceGenerator(String word){
        int numberOfWordsInGrimace = random.nextInt(2) + 1;
        int putWordAfter = random.nextInt(numberOfWordsInGrimace);
        StringBuilder grimace = new StringBuilder();
        for (int i = 0; i <= numberOfWordsInGrimace; i++) {
            if (i != 0)
                grimace.append(" ");
            int randomIndex = random.nextInt(minionGrimaces.size());
            grimace.append(minionGrimaces.get(randomIndex));
            if (putWordAfter == i){
                grimace.append(" ").append(word);
            }
        }
        return grimace.toString();
    }
}
