package com.reactiveProgramming.webflux.repositories;

import com.reactiveProgramming.webflux.entities.Book;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BookRepository extends ReactiveCrudRepository<Book,Integer>
{
    @Query("SELECT * FROM book WHERE book_name LIKE CONCAT('%', :name, '%')")
    Flux<Book> findByBookNameLike(@Param("name") String name);

}

