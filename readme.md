
# 🌀 Understanding `Mono` in Project Reactor

> **Source of truth:** [Project Reactor Documentation](https://projectreactor.io/docs/core/release/reference/)
> **Simplified & explained for learners.**

---

## 🌱 1. What is a `Mono`?

A **`Mono`** is a *Reactive Streams Publisher* that can emit **at most one item**.

According to the official docs:

> “A Reactive Streams `Publisher` with basic rx operators that emits at most one item via the `onNext` signal then terminates with an `onComplete` signal … or only emits a single `onError` signal.”
> — [Reactor Core API Docs](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Mono.html)

Simply put:

* It **may emit one value**,
* Or **emit nothing**,
* And then **complete or fail**.

Think of it as a **reactive container** that holds **zero or one** value.

---

## 🧠 2. Mental Model — *The “Single-Ticket Bus Ride”*

To build an intuition, imagine `Mono` as a **bus that only allows one passenger**:

1. When you **subscribe**, you board the bus.
2. The bus can carry **one passenger at most**.
3. If there’s a passenger, it drops them off → `onNext(value)` → `onComplete()`.
4. If something goes wrong → `onError(error)`.
5. If no one was there → it still finishes the ride empty → `onComplete()`.

So, `Mono` represents **a computation or data source that may produce one value — or none — asynchronously.**

---

## ⚙️ 3. How `Mono` Works (Step-by-Step)

Let’s simplify the reactive flow between **Publisher** and **Subscriber**:

1. A `Mono<T>` is **created**:

   ```java
   Mono<String> mono = Mono.just("Hello Reactor");
   ```

2. A **Subscriber** subscribes:

   ```java
   mono.subscribe(value -> System.out.println("Received: " + value));
   ```

3. The sequence of events:

    * Publisher calls → `onSubscribe(Subscription s)`
    * Subscriber requests one item → `s.request(1)`
    * Publisher sends → `onNext(value)`
    * Then → `onComplete()`
    * If an error occurs → `onError(error)`

### 👉 Signals a `Mono` can emit:

| Signal Type            | Description                   |
| ---------------------- | ----------------------------- |
| `onNext(T value)`      | Sends one value downstream    |
| `onComplete()`         | Signals that emission is done |
| `onError(Throwable e)` | Signals that something failed |

After a `Mono` terminates, **no more signals** are sent.

---

## 🧩 4. Common Mono Variants

| Type                    | Description                        |
| ----------------------- | ---------------------------------- |
| `Mono.just(T)`          | Emits a single value and completes |
| `Mono.empty()`          | Emits no value, just completes     |
| `Mono.error(Throwable)` | Immediately terminates with error  |
| `Mono.never()`          | Emits nothing and never terminates |

Example:

```java
Mono<String> hello = Mono.just("Hello");
Mono<Void> nothing = Mono.empty();
Mono<String> failure = Mono.error(new RuntimeException("Oops!"));
```

---

## 🧪 5. Example Test (Spring Boot)

Here’s a **Spring Boot test** that demonstrates subscribing to a Mono:

```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class MonoExampleTest {

    @Test
    void testMonoEmitsValue() {
        Mono<String> mono = Mono.just("Reactor Rocks!");

        StepVerifier.create(mono)
                .expectNext("Reactor Rocks!")
                .verifyComplete();
    }

    @Test
    void testMonoEmpty() {
        Mono<String> mono = Mono.empty();

        StepVerifier.create(mono)
                .verifyComplete(); // no value, just completes
    }
}
```

🧩 **Explanation:**

* `StepVerifier` is a testing tool from Project Reactor to test publishers like Mono/Flux.
* `expectNext()` checks the emitted value.
* `verifyComplete()` ensures the publisher completed successfully.

---

## ⚡ 6. Operator Behavior

The official docs note that:

> “Most `Mono` operators preserve the ‘at most one’ property. For instance, `flatMap` returns a `Mono`, while `flatMapMany` can produce multiple elements.”

✅ `flatMap()` → keeps one result
✅ `map()` → transforms the result
⚠️ `flatMapMany()` → converts to `Flux` (multi-item stream)

Example:

```java
Mono<String> mono = Mono.just("Reactor")
        .map(v -> v.toUpperCase()); // still Mono<String>
```

---

## 🚧 7. Nuances (from the Docs)

* A `Mono` **must not** emit both `onNext` and `onError`.
  Once one is called, the sequence ends.
* `Mono<Void>` is often used when the result doesn’t matter (e.g., save operation).
* Avoid mutable state inside lambdas used in Mono operators — they might be shared between subscribers.

---

## 💡 8. When to Use `Mono`

Use `Mono` when your logic produces **a single result** or **none**:

✅ Examples:

* Fetching a **single user** from a database
* Returning **one HTTP response**
* Executing **a command that has no return value**

Use `Flux` instead if you expect multiple results.

---

## 🧭 9. Mono vs Flux (Quick Recap)

| Type      | Emits           | Example                  |
| --------- | --------------- | ------------------------ |
| `Mono<T>` | 0 or 1 item     | Fetching one user        |
| `Flux<T>` | 0 or many items | Streaming multiple users |

---

## 🪄 10. Summary

| Concept       | Description                                         |
| ------------- | --------------------------------------------------- |
| **Mono**      | Reactive container for 0 or 1 element               |
| **Signals**   | `onNext`, `onComplete`, `onError`                   |
| **Lifecycle** | Create → Subscribe → Emit → Complete/Error          |
| **Purpose**   | Non-blocking async operation that yields one result |
| **Analogy**   | A bus that can carry only one passenger at most     |

---

## 🧠 In One Line

> **Mono is like a promise that might deliver one result — or none — and does so reactively.**

---


## 🧭 1. Creation Methods — “Starting a Mono”

These are used to **instantiate** or **generate** a `Mono`.

| Method                                  | Description                                                    | Example                                                   |
| --------------------------------------- | -------------------------------------------------------------- | --------------------------------------------------------- |
| `Mono.just(T)`                          | Emits a **single value** and completes.                        | `Mono.just("Hello")`                                      |
| `Mono.empty()`                          | Completes without emitting a value.                            | `Mono.empty()`                                            |
| `Mono.error(Throwable)`                 | Immediately emits an error signal.                             | `Mono.error(new RuntimeException("Failed"))`              |
| `Mono.never()`                          | Never emits or completes — used in testing or control flows.   | `Mono.never()`                                            |
| `Mono.fromCallable(Supplier<T>)`        | Defers execution of blocking code (e.g., DB read).             | `Mono.fromCallable(() -> readFile())`                     |
| `Mono.fromSupplier(Supplier<T>)`        | Similar to `just()`, but **lazy** — runs only on subscription. | `Mono.fromSupplier(() -> "Reactive")`                     |
| `Mono.fromFuture(CompletableFuture<T>)` | Adapts Java’s `CompletableFuture` into a `Mono`.               | `Mono.fromFuture(fetchDataAsync())`                       |
| `Mono.defer(Supplier<Mono<T>>)`         | Delays creation of the actual Mono until subscription.         | `Mono.defer(() -> Mono.just(System.currentTimeMillis()))` |

### 💡 Tip:

* Use `just()` for **constant** data.
* Use `fromSupplier()` or `defer()` for **dynamic** or **time-sensitive** data.
* Use `fromCallable()` for potentially **blocking** work, wrapped reactively.

---

## 🔄 2. Transformation Methods — “Changing the Data”

These methods act like **pipes** in your reactive flow.

| Method                                   | Behavior                                   | Example                                                |
| ---------------------------------------- | ------------------------------------------ | ------------------------------------------------------ |
| `map(Function<T, R>)`                    | Synchronously transforms the value.        | `Mono.just(5).map(i -> i * 2)` → `10`                  |
| `flatMap(Function<T, Mono<R>>)`          | Asynchronously transforms to another Mono. | `Mono.just("user").flatMap(repo::findById)`            |
| `flatMapMany(Function<T, Publisher<R>>)` | Converts `Mono<T>` → `Flux<R>`.            | `Mono.just("id").flatMapMany(repo::findPostsByUserId)` |
| `filter(Predicate<T>)`                   | Emits only if predicate is true.           | `Mono.just(5).filter(i -> i > 10)` → `Mono.empty()`    |
| `defaultIfEmpty(T)`                      | Provides fallback if source is empty.      | `Mono.empty().defaultIfEmpty("Guest")`                 |
| `switchIfEmpty(Mono<T>)`                 | Switches to another Mono if empty.         | `Mono.empty().switchIfEmpty(Mono.just("Backup"))`      |

### ⚙️ Example:

```java
Mono.just("spring")
    .map(String::toUpperCase)
    .flatMap(s -> Mono.just(s + " WEBFLUX"))
    .subscribe(System.out::println); 
// Output: SPRING WEBFLUX
```

---

## ⚡ 3. Combination Methods — “Composing Multiple Monos”

| Method                  | Description                                              | Example                                     |
| ----------------------- | -------------------------------------------------------- | ------------------------------------------- |
| `then(Mono<V>)`         | Waits for completion, then runs another Mono.            | `saveUser().then(sendEmail())`              |
| `zip(Mono<A>, Mono<B>)` | Combines results into a tuple.                           | `Mono.zip(getUser(), getProfile())`         |
| `zipWith(Mono<B>)`      | Same as `zip`, but instance-based.                       | `getUser().zipWith(getProfile())`           |
| `concatWith(Mono<T>)`   | Sequentially executes Monos (first → second).            | `Mono.just("A").concatWith(Mono.just("B"))` |
| `when(Mono... monos)`   | Run multiple Monos in parallel; wait until all complete. | `Mono.when(task1, task2, task3)`            |

### 💡 Tip:

Use `zip()` when you need **both results**,
and `then()` when you only care about **completion order**.

---

## 🚨 4. Error Handling Methods — “Resilience Layer”

| Method                                        | Purpose                             | Example                                    |
| --------------------------------------------- | ----------------------------------- | ------------------------------------------ |
| `onErrorReturn(T)`                            | Return a fallback value.            | `.onErrorReturn("default")`                |
| `onErrorResume(Function<Throwable, Mono<T>>)` | Switch to another Mono dynamically. | `.onErrorResume(e -> Mono.just("Backup"))` |
| `onErrorMap(Function<Throwable, Throwable>)`  | Transform one error into another.   | `.onErrorMap(IOException::new)`            |
| `doOnError(Consumer<Throwable>)`              | Side effect logging/tracing.        | `.doOnError(e -> log.error("Fail", e))`    |

### Example:

```java
Mono.just("data")
    .flatMap(this::callExternalApi)
    .onErrorResume(e -> Mono.just("Fallback data"))
    .subscribe(System.out::println);
```

---

## 🧩 5. Side-Effect Methods — “For Logging and Debugging”

| Method                                                                   | Description |
| ------------------------------------------------------------------------ | ----------- |
| `doOnNext(Consumer<T>)` – peek into each emission                        |             |
| `doOnSubscribe(Consumer<Subscription>)` – trigger action when subscribed |             |
| `doOnSuccess(Consumer<T>)` – for success logging                         |             |
| `doFinally(SignalType)` – runs on termination (complete/error/cancel)    |             |

### Example:

```java
Mono.just("Reactor")
    .doOnSubscribe(sub -> System.out.println("Subscribed"))
    .doOnNext(val -> System.out.println("Value: " + val))
    .doOnSuccess(v -> System.out.println("Done"))
    .subscribe();
```

---

## 🚧 6. Terminal Methods — “Triggering Execution”

Remember: **nothing happens until subscription**.

| Method                                                                | Effect |
| --------------------------------------------------------------------- | ------ |
| `subscribe()` – Starts the pipeline                                   |        |
| `block()` – Converts async Mono → blocking call (avoid in production) |        |
| `toFuture()` – Converts Mono → CompletableFuture                      |        |
| `subscribe(Consumer<T>)` – Reacts to emitted data                     |        |

---

## 🧠 Quick Recap

| Category       | Purpose      | Example               |
| -------------- | ------------ | --------------------- |
| Creation       | Start a Mono | `Mono.just(42)`       |
| Transformation | Change data  | `.map()` `.flatMap()` |
| Combination    | Join Monos   | `.zip()` `.then()`    |
| Error Handling | Recovery     | `.onErrorResume()`    |
| Side Effects   | Logging      | `.doOnNext()`         |
| Terminal       | Execute      | `.subscribe()`        |

---

## 🎯 Exercise

Try this in your IDE:

```java
Mono.just("Reactor")
    .map(String::toUpperCase)
    .flatMap(s -> Mono.just(s + " CORE"))
    .filter(s -> s.contains("CORE"))
    .doOnNext(System.out::println)
    .then(Mono.just("Completed!"))
    .subscribe(System.out::println);
```

Can you predict:

1. What’s printed in the console?
2. What would happen if you replaced `.then()` with `.zipWith()`?

---



