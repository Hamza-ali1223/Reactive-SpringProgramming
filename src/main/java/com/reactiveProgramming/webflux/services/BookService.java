package com.reactiveProgramming.webflux.services;


import com.reactiveProgramming.webflux.entities.Book;
import com.reactiveProgramming.webflux.repositories.BookRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookService
{
    public final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Flux<Book> getBooks()
    {
        return bookRepository.findAll();

    }

    public Mono<Book> saveBook(Book book)
    {
        return bookRepository.save(book);
    }
    public Mono<Book> getBookById(int id)
    {
        return bookRepository.findById(id);
    }

    public Mono<Void> deleteBookById(int id)
    {
        return bookRepository.deleteById(id);
    }

    public Flux<Book> searchBookByName(String name)
    {
        return bookRepository.findByBookNameLike(name);
    }
}
