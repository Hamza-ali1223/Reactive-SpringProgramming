package com.reactiveProgramming.webflux;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

@SpringBootTest
class WebfluxApplicationTests {

    @Test
    void testMonoPublisherAndSubscriber() {

        // Step 1: Create a Publisher (Mono)
        Mono<String> publisher = Mono.just("Hello Reactive World");

        // Step 2: Create a custom Subscriber
        Subscriber<String> subscriber = new Subscriber<>() {

            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("✅ onSubscribe called");
                this.subscription = s;
                // Step 3: Request data from Publisher
                subscription.request(1);
            }

            @Override
            public void onNext(String item) {
                System.out.println("📦 Received data: " + item);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("❌ Error occurred: " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("🏁 Completed receiving data");
            }
        };

        // Step 4: Subscriber subscribes to Publisher
        publisher.subscribe(subscriber);
    }

}
