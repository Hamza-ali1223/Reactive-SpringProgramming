package com.reactiveProgramming.webflux;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;



@SpringBootTest
class WebfluxApplicationTests {

    @Test
    void MonoUsage() {
        Mono.just("Reactor")
                .map(String::toUpperCase)
                .flatMap(s -> Mono.just(s + " CORE"))
                .filter(s -> s.contains("CORE"))
                .doOnNext(System.out::println)
                .then(Mono.just("Completed!"))
                .subscribe(System.out::println);


    }

}
