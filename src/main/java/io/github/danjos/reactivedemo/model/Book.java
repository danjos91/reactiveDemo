package io.github.danjos.reactivedemo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Book {
    @Id
    private Integer id;
    private String title;

    public Book() {
    }

    public Book(String title) {
        this.title = title;
    }
}
        
    // геттеры-сеттеры