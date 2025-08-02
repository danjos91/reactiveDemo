package io.github.danjos.reactivedemo.service;

import io.github.danjos.reactivedemo.model.Book;
import io.github.danjos.reactivedemo.repository.BookRepository;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final Logger logger = LoggerFactory.getLogger(BookService.class);

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Mono<Void> saveAndDeleteBook(String name) {
        return bookRepository.save(new Book(name)) // 1. Сохраняем книгу с именем "name"
                .doOnNext(book -> logger.info("Книга сохранена с ID: {}", book.getId())) // 2. Выводим ID сохранённой книги
                .flatMap(book -> bookRepository.delete(book) // 3. Удаляем созданную книгу
                    .then(bookRepository.existsById(book.getId()))) // 4. Проверяем, существует ли книга
                .doOnNext(exists -> logger.info("Книга существует после удаления: {}", exists)) // Выводим результат проверки
                .then();
    }
}
