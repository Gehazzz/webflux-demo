package com.example.minionreactive.router.handler;

import com.example.minionreactive.model.Word;
import com.example.minionreactive.router.exception.ServerWebInputValidationException;
import com.example.minionreactive.service.ReactiveWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@Component
public class WordsRequestHandlerExceptionHandling {
    @Autowired
    private ReactiveWordsService wordsService;
    @Autowired
    private Validator validator;

    public Mono<ServerResponse> translateHandlerWithValidation(ServerRequest serverRequest) {
        /*String word = serverRequest.pathVariable("word");
        validateSimple(word);
        Mono<Word> translated = wordsService.translate(word);
        return ServerResponse.ok().body(translated, Word.class);*/
        /*Mono<Word> translated = Mono.just(serverRequest.pathVariable("word"))
                .flatMap(this::validateSimple)
                .flatMap(wordsService::translate);
        return ServerResponse.ok().body(translated, Word.class);*/

        return Mono.just(serverRequest.pathVariable("word"))
                .doOnNext(this::validateSimple2)
                .flatMap(wordsService::translate)
                .flatMap(word -> ServerResponse.ok().bodyValue(word));
    }

    public Mono<ServerResponse> addWordHandlerWithValidation(ServerRequest serverRequest) {
        Mono<Word> wordMono = serverRequest
                .bodyToMono(Word.class)
                .flatMap(this::validateBean);
        Mono<Word> wordMonoResponse = wordsService.addWord(wordMono);
        return ServerResponse.ok().body(wordMonoResponse, Word.class);
    }

    public Mono<ServerResponse> addWordHandlerWithValidation2(ServerRequest serverRequest) {
        Mono<Word> wordMono = serverRequest
                .bodyToMono(Word.class)
                .doOnNext(this::validateBean2);
        Mono<Word> wordMonoResponse = wordsService.addWord(wordMono);
        return wordMonoResponse.flatMap(word -> ServerResponse.ok().body(word, Word.class));
    }

    private Mono<String> validateSimple(String limit) {
        if (limit.length() > 1 && limit.length() < 20)
            return Mono.error(new ServerWebInputException("word must be between 1 and 20 characters"));
        return Mono.just(limit);
    }

    private void validateSimple2(String limit) {
        if (limit.length() > 1 && limit.length() < 20)
            throw new ServerWebInputException("word must be between 1 and 20 characters");
    }

    private Mono<Word> validateBean(Word word) {
        Errors errors = new BeanPropertyBindingResult(word, "word");
        validator.validate(word, errors);
        if (errors.hasErrors()) {
            return Mono.error(new ServerWebInputException(errors.toString())); // (3)
        }
        return Mono.just(word);
    }

    private void validateBean2(Word word) {
        Errors errors = new BeanPropertyBindingResult(word, "word");
        validator.validate(word, errors);
        if (errors.hasErrors())
            throw new ServerWebInputValidationException(errors);
    }

    /**
     * https://www.baeldung.com/spring-functional-endpoints-validation
     * https://github.com/spring-projects/spring-framework/blob/main/src/docs/asciidoc/web/webflux-functional.adoc#validation
     *  https://stackoverflow.com/questions/57026777/how-to-validate-a-mapstring-string-using-spring-validator-programmatically
     * @param limit
     */

/*    private void validate(String limit) {


        Map<String, String> limit1 = Map.of("limit", limit);
        Validator v = new Validator() {
            @Override
            public boolean supports(Class<?> clazz) {
                return Map.class.isAssignableFrom(clazz);
            }

            @Override
            public void validate(Object target, Errors errors) {
                Map<String, String> str = (Map<String, String>) target;
                errors.rejectValue(str.get("limit"), "errorrrrrrrs");
            }
        };
        DataBinder binder = new DataBinder(limit1);
        binder.setValidator(v);
        binder.validate();
    }

    public void validate2(String limit) {
        Validator v = new Validator() {
            @Override
            public boolean supports(Class<?> clazz) {
                return Map.class.isAssignableFrom(clazz);
            }

            @Override
            public void validate(Object target, Errors errors) {
                Map<String, String> str = (Map<String, String>) target;
                errors.rejectValue(str.get("limit"), "errorrrrrrrs");
            }
        };
        Map<String, String> limit1 = Map.of("limit", limit);
        //Errors errors = new MapBindingResult(limit1, "limit");
        BindingResult errors = new MapBindingResult(limit1, "limit");
        v.validate(limit1, errors);
        Method m = Arrays.stream(WordsRequestHandler.class.getMethods()).filter(method -> method.getName().equals("validate2")).findAny().get();

        if (errors.hasErrors()) {
            //ServerWebInputException s =  new ServerWebInputException(errors.toString());
            throw new WebExchangeBindException(new MethodParameter(m, 0), errors); // (3)
        }
    }*/
}
