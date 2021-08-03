package com.example.minionreactive.router;

import com.example.minionreactive.controller.advice.ValidationErrorResponse;
import com.example.minionreactive.controller.advice.Violation;
import com.example.minionreactive.router.exception.ServerWebInputValidationException;
import com.example.minionreactive.router.handler.WordsRequestHandler;
import com.example.minionreactive.router.handler.WordsRequestHandlerExceptionHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Component
@Configuration
public class WordsRouter {
    @Autowired
    private WordsRequestHandler handler;
    @Autowired
    private WordsRequestHandlerExceptionHandling validationHandler;

    @Bean
    public RouterFunction<ServerResponse> highLevelRouter(){
        return RouterFunctions.route()
                .path("router", this::serverResponseRouterFunction)
                .build();
    }

    //@Bean
    public RouterFunction<ServerResponse> serverResponseRouterFunction() {
        return RouterFunctions.route()
                .GET("words/translate/{word}", RequestPredicates.path("*/1?").or(RequestPredicates.path("*/20")),handler::translateHandler)
                .GET("words/translate/{word}", req -> ServerResponse.badRequest().bodyValue("word must be between 1 and 20 characters"))
                .GET("words/rhymes/{word}", handler::rhymesHandler)
                .GET("words/rhymes/{word}", handler::rhymesHandler)
                .GET("words/stream/rhymes/{word}", handler::rhymesStreamHandler)
                .POST("words", handler::addWordHandler)
                .GET("words-exception-handling/translate/{word}", validationHandler::translateHandlerWithValidation)
                .POST("words-exception-handling", validationHandler::addWordHandlerWithValidation2)
                .onError(ServerWebInputValidationException.class, exceptionHandlerJsr())
                .onError(ServerWebInputException.class, exceptionHandler())
                .build();
    }

    private BiFunction<ServerWebInputException, ServerRequest, Mono<ServerResponse>> exceptionHandler() {
        return (throwable, serverRequest) -> {
            ValidationErrorResponse error = new ValidationErrorResponse();
                ServerWebInputException err = throwable;
                error.getViolations().add(
                        new Violation(err.getReason(), err.getMessage()));

            return ServerResponse.badRequest().bodyValue(error);
        };
    }

    private BiFunction<ServerWebInputValidationException, ServerRequest, Mono<ServerResponse>> exceptionHandlerJsr() {
        return (serverWebInputValidationException, serverRequest) -> {
            ValidationErrorResponse error = new ValidationErrorResponse();
            serverWebInputValidationException.getErrors().forEach((field, message) -> {
                error.getViolations().add(
                        new Violation(field, message));
            });
            return ServerResponse.badRequest().bodyValue(error);
        };
    }
}
