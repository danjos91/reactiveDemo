package io.github.danjos.reactivedemo.service;

import io.github.danjos.reactivedemo.model.Book;
import io.github.danjos.reactivedemo.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        // Clean up the database before each test
        bookRepository.deleteAll().block();
    }

    @Test
    void testSaveAndDeleteBook() {
        // Given
        String bookName = "Test Book";

        // When
        Mono<Void> result = bookService.saveAndDeleteBook(bookName);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        // Verify that no books remain in the database
        StepVerifier.create(bookRepository.count())
                .expectNext(0L)
                .verifyComplete();
    }

    @Test
    void testSaveAndDeleteBookWithVerification() {
        // Given
        String bookName = "Another Test Book";

        // When & Then
        StepVerifier.create(bookService.saveAndDeleteBook(bookName))
                .verifyComplete();

        // Additional verification: check that the book was actually deleted
        StepVerifier.create(bookRepository.findAll())
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void testSaveAndDeleteBookMultipleTimes() {
        // Given
        String bookName1 = "First Book";
        String bookName2 = "Second Book";

        // When & Then
        StepVerifier.create(bookService.saveAndDeleteBook(bookName1))
                .verifyComplete();

        StepVerifier.create(bookService.saveAndDeleteBook(bookName2))
                .verifyComplete();

        // Verify database is empty
        StepVerifier.create(bookRepository.count())
                .expectNext(0L)
                .verifyComplete();
    }

    @Test
    public void testCreateBook() {
        bookService.createBook("Реактивное программирование в Java")
                .doOnNext(book -> Assertions.assertThat(book)
                        .withFailMessage("Результат сохранения не должен быть пустым")
                        .isNotNull()
                        .withFailMessage("Сохранённой книге должен быть присвоен ID")
                        .extracting(Book::getId)
                        .isNotNull()
                ).block(); // блокируемся на тестовом потоке до завершения реактивной цепочки
    }
}