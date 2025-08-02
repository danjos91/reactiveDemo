package io.github.danjos.reactivedemo.repository;

import io.github.danjos.reactivedemo.model.Book;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends ReactiveCrudRepository<Book, Integer> {
}
