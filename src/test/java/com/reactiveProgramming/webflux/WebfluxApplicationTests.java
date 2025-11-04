package com.reactiveProgramming.webflux;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;


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

    @Test
    void repeatAndZipExample() {
        Flux<String> nameFlux = Flux.just("Hamza", "Wali", "Izaan", "Ali")
                .repeat(); // repeat indefinitely

        Flux<Integer> numberFlux = Flux.just(1,2,3,4,5,5,6,7,7,234,5,2324,223,450);

        Flux<Tuple2<String, Integer>> zippedFlux =
                nameFlux.zipWith(numberFlux, Tuples::of);

        zippedFlux.subscribe(tuple ->
                System.out.println(tuple.getT1() + " → " + tuple.getT2())
        );
    }


}
