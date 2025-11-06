package com.reactiveProgramming.webflux.controllers;


import com.reactiveProgramming.webflux.entities.Book;
import com.reactiveProgramming.webflux.services.BookService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/books")
public class BookController
{
    public final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @CrossOrigin
    @GetMapping()
    public Flux<Book> findAll()
    {
        return bookService.getBooks();
    }

    @GetMapping("/{id}")
    public Mono<Book> findById(@PathVariable int id)
    {
        return bookService.getBookById(id);
    }

    @PostMapping
    public Mono<Book> create(@RequestBody Book book)
    {
        return bookService.saveBook(book);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable int id)
    {
        return bookService.deleteBookById(id);
    }

    @GetMapping("/{bookname}")
    public Flux<Book> findByBookName(@PathVariable String bookname)
    {
        return bookService.searchBookByName(bookname);
    }
}
