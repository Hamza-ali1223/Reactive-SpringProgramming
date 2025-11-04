
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


# 🌊 Understanding `Flux` in Project Reactor

> **Source of truth:** [Project Reactor Documentation](https://projectreactor.io/docs/core/release/reference/)
> **Based on "Master Flux Tutorial" by Learn Code With Durgesh.**

-----

## 🌱 1. What is a `Flux`?

A **`Flux`** is a *Reactive Streams Publisher* that can emit **zero or many items** (`0..N`).

As explained in the tutorial, a `Flux` is an object that represents a stream of 0 to N elements. It's the reactive equivalent of a list or a sequence of events over time.

Simply put:

* It **may emit multiple values** (or none).
* It sends an `onNext` signal for each value.
* It **completes** with an `onComplete` signal or **fails** with an `onError` signal.

-----

## 🧠 2. Mental Model — *The “Streaming Playlist”*

To build an intuition, imagine `Flux` as a **streaming music playlist**:

1.  When you **subscribe** (`.subscribe()`), you hit the "play" button.
2.  The playlist can send you **many songs** (values), one after the other.
3.  Each song arriving is an `onNext(song)` signal.
4.  If something goes wrong (e.g., lost connection) → `onError(error)`.
5.  When the playlist finishes all songs → it signals `onComplete()`.

So, `Flux` represents **a computation or data source that produces a sequence of values asynchronously.**

-----

## ⚙️ 3. How `Flux` Works (Step-by-Step)

Let’s simplify the reactive flow based on the video's `log()` demonstration:

1.  A `Flux<T>` is **created** (e.g., `Flux.just("Ankit", "Durgesh")`).
2.  A **Subscriber** subscribes (e.g., `flux.subscribe(...)`).
3.  The sequence of events:
   * Publisher calls → `onSubscribe(Subscription s)` (The connection is made).
   * Subscriber requests data → `s.request(unbounded)` (Subscriber says "send me everything").
   * Publisher sends first item → `onNext("Ankit")`
   * Publisher sends second item → `onNext("Durgesh")`
   * ... (this repeats for all items)
   * Publisher finishes → `onComplete()`
   * If an error occurs → `onError(error)`

### 👉 Signals a `Flux` can emit:

| Signal Type | Description |
| :--- | :--- |
| `onNext(T value)` | Sends one value downstream (can be called many times) |
| `onComplete()` | Signals that the stream is done |
| `onError(Throwable e)`| Signals that something failed |

-----

## 🧭 1. Creation Methods — “Starting a Flux”

These are used to **instantiate** or **generate** a `Flux` from a source.

| Method | Description | Example (from video) |
| :--- | :--- | :--- |
| **`Flux.just(T... values)`** | Emits a fixed sequence of items and completes. | `Flux.just("Ankit", "Durgesh", "Ravi")` |
| **`Flux.fromIterable(Iterable<T>)`** | Emits elements from a Java `Iterable` (like `List`). | `Flux.fromIterable(List.of("Mango", "Apple"))` |
| **`Flux.empty()`** | Completes without emitting any value. | `Flux.empty()` |

### 🧪 Example Test (`just`):

```java
@Test
void simpleFluxTest() {
    Flux<String> nameFlux = Flux.just("Ankit", "Durgesh", "Ravi", "Gautam")
            .log(); // Log all signals

    nameFlux.subscribe(name -> {
        System.out.println("Received: " + name);
    });
}
```

**Output (simplified from video):**

```
[info] onSubscribe(FluxMapFuseable.MapFuseableSubscriber)
[info] request(unbounded)
[info] onNext(Ankit)
Received: Ankit
[info] onNext(Durgesh)
Received: Durgesh
[info] onNext(Ravi)
Received: Ravi
[info] onNext(Gautam)
Received: Gautam
[info] onComplete()
```

-----

## 🔄 2. Transformation Methods — “Changing the Data”

These methods transform the items in the stream.

| Method | Behavior | Example (from video) |
| :--- | :--- | :--- |
| **`map(Function<T, R>)`** | Synchronously transforms each value. | `getFlux().map(name -> name.toUpperCase())` |
| **`filter(Predicate<T>)`** | Emits only if the predicate (condition) is true. | `getFlux().filter(name -> name.length() > 4)` |
| **`flatMap(Function<T, Mono<R>>)`** | Asynchronously transforms to another Publisher (Mono/Flux) and *interleaves* the results. | `getFlux().flatMap(name -> Flux.just(name.split("")))` |
| **`transform(Function<Flux<T>, P>)`** | Applies a reusable function (logic block) to the entire Flux. | `getFlux().transform(functionInterface)` |
| **`delayElements(Duration)`** | Introduces a time delay between each element's emission. | `getFlux().delayElements(Duration.ofSeconds(2))` |

### 🧪 Example Test (`filter`):

The video demonstrates filtering names with a length greater than 4.

```java
@Test
void filterTest() {
    Flux<String> nameFlux = Flux.just("Ankit", "Durgesh", "Ravi", "Gautam")
            .filter(name -> name.length() > 4)
            .log();

    // StepVerifier is used to test reactive streams
    StepVerifier.create(nameFlux)
            .expectNext("Ankit", "Durgesh", "Gautam") // "Ravi" is filtered out
            .verifyComplete();
}
```

**Output (simplified from video):**

```
[info] onNext(Ankit)
[info] onNext(Durgesh)
[info] onNext(Gautam)
[info] onComplete()
// Test passes
```

-----

## ⚡ 3. Combination Methods — “Composing Multiple Fluxes”

These methods join two or more Flux streams together.

| Method | Description | Behavior |
| :--- | :--- | :--- |
| **`concat(Publisher<T>...)`** | Joins streams sequentially. Waits for the first to complete before subscribing to the next. | **Synchronous / Order-Preserving** |
| **`concatWith(Publisher<T>)`** | Instance version of `concat`. | **Synchronous / Order-Preserving** |
| **`merge(Publisher<T>...)`** | Joins streams as they emit. Subscribes to all at once and interleaves elements. | **Asynchronous / Not Ordered** |
| **`mergeWith(Publisher<T>)`** | Instance version of `merge`. | **Asynchronous / Not Ordered** |
| **`zip(Publisher<A>, Publisher<B>)`** | Combines elements pairwise into a `Tuple`. Stops when the *shortest* stream completes. | **Synchronous / Pairwise** |
| **`zipWith(Publisher<B>)`** | Instance version of `zip`. | **Synchronous / Pairwise** |

### 🧪 Example Test (`concat` vs `merge`):

The video clearly shows the difference using `delayElements`.

```java
// CONCAT (Synchronous)
Flux<String> names = Flux.just("Ankit", "Durgesh")
        .delayElements(Duration.ofSeconds(1));
Flux<String> fruits = Flux.just("Mango", "Apple")
        .delayElements(Duration.ofSeconds(2));

Flux<String> concatFlux = Flux.concat(names, fruits).log();

// MERGE (Asynchronous)
Flux<String> mergeFlux = Flux.merge(names, fruits).log();
```

**Output (`concatFlux`):**
*(Waits for "names" to finish, then starts "fruits")*

```
onNext(Ankit)     // 1s
onNext(Durgesh)    // 2s
onNext(Mango)      // 4s (waited for names + 2s delay)
onNext(Apple)      // 6s (waited for names + 4s delay)
onComplete()
```

**Output (`mergeFlux`):**
*(Subscribes to both at once, elements arrive as they are ready)*

```
onNext(Ankit)     // 1s
onNext(Durgesh)    // 2s
onNext(Mango)      // 2s
onNext(Apple)      // 4s
onComplete()
```

*(Note: In the video, "Mango" (2s) arrived *before* "Durgesh" (2s) due to thread scheduling, highlighting the async nature.)*

-----

## 🚨 4. Default & Error Handling Methods — “Resilience Layer”

These methods provide fallbacks for empty or failed streams.

| Method | Purpose | Example (from video) |
| :--- | :--- | :--- |
| **`defaultIfEmpty(T)`** | Emits a single default value if the source Flux is empty. | `getFlux().filter(l -> l > 8).defaultIfEmpty("No Name")` |
| **`switchIfEmpty(Publisher<T>)`** | Switches to a *different* Flux (a fallback stream) if the source is empty. | `getFlux().filter(l > 8).switchIfEmpty(fruitFlux)` |
| **`onErrorMap`** | (Mentioned) Transform one error into another. | |
| **`onErrorResume`** | (Mentioned) Switch to a fallback Publisher on error. | |

### 🧪 Example Test (`switchIfEmpty`):

```java
@Test
void switchIfEmptyTest() {
    Flux<String> nameFlux = Flux.just("Ankit", "Durgesh", "Ravi", "Gautam")
            .filter(name -> name.length() > 8); // This filter makes the Flux empty

    Flux<String> fruitFlux = Flux.just("Mango", "Apple");

    Flux<String> fallbackFlux = nameFlux.switchIfEmpty(fruitFlux).log();

    StepVerifier.create(fallbackFlux)
            .expectNext("Mango", "Apple") // It switched to the fruitFlux
            .verifyComplete();
}
```

**Output (simplified from video):**

```
[info] onSubscribe(...)
[info] request(unbounded)
[info] onNext(Mango)
[info] onNext(Apple)
[info] onComplete()
```

-----

## 🧩 5. Side-Effect Methods — “For Logging and Debugging”

These methods (also called "do on" operators) allow you to peek into the stream without changing it.

| Method | Description |
| :--- | :--- |
| **`log()`** | Logs all signals (onSubscribe, onNext, onComplete, etc.) to the console. |
| **`doOnNext(Consumer<T>)`** | Triggers a side effect for each `onNext` signal. |
| **`doOnSubscribe(Consumer<Subscription>)`** | Triggers a side effect when the subscription first occurs. |
| **`doOnEach(Consumer<Signal<T>>)`** | Triggers a side effect for *every* signal (onNext, onComplete, onError). |
| **`doOnComplete(Runnable)`** | Triggers a side effect when the `onComplete` signal occurs. |

### 🧪 Example Test (`doOn` operators):

```java
@Test
void sideEffectTest() {
    Flux<String> nameFlux = Flux.just("Ankit", "Durgesh")
            .doOnSubscribe(sub -> System.out.println("-> Subscribed!"))
            .doOnNext(name -> System.out.println("-> doOnNext: " + name))
            .doOnComplete(() -> System.out.println("-> Completed!"))
            .log();

    nameFlux.subscribe(name -> System.out.println("Received: " + name));
}
```

**Output (simplified from video &):**

```
-> Subscribed!
[info] onSubscribe(...)
[info] request(unbounded)
-> doOnNext: Ankit
[info] onNext(Ankit)
Received: Ankit
-> doOnNext: Durgesh
[info] onNext(Durgesh)
Received: Durgesh
-> Completed!
[info] onComplete()
```

*(Note: The `doOn` methods execute *before* the signal is passed downstream, as shown in the video.)*

-----

## 🚧 6. Terminal Methods — “Triggering Execution”

Nothing happens until you subscribe. These methods trigger the stream.

| Method | Effect |
| :--- | :--- |
| **`subscribe(Consumer<T>)`** | The primary way to start the stream and consume elements. |
| **`StepVerifier.create(Publisher<T>)`** | A non-blocking terminal operator *for testing* that verifies the stream's emissions. |

-----

## 💡 8. When to Use `Flux`

Use `Flux` when your logic produces **zero, one, or many results** over time:

✅ **Examples:**

* Streaming **all users** from a database
* Reading a **file line by line**
* Receiving **a stream of live data** (e.g., stock tickers, tweets)
* Returning a **list of items** from an HTTP endpoint



