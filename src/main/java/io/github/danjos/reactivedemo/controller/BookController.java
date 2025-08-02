package io.github.danjos.reactivedemo.controller;

import io.github.danjos.reactivedemo.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/save-and-delete")
    public Mono<ResponseEntity<String>> saveAndDeleteBook(@RequestParam String name) {
        return bookService.saveAndDeleteBook(name)
                .then(Mono.just(ResponseEntity.ok("Book '" + name + "' was saved and then deleted successfully")))
                .onErrorReturn(ResponseEntity.internalServerError().body("Error processing book: " + name));
    }

    @GetMapping("/test")
    public Mono<ResponseEntity<String>> testEndpoint() {
        return Mono.just(ResponseEntity.ok("Book service is working!"));
    }
} 