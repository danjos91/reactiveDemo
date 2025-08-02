package io.github.danjos.reactivedemo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
public class Account {
    @Id
    private Integer id;
    private String name;
    private BigDecimal balance = BigDecimal.valueOf(10_000L);

    public Account() {
    }

    public Account(String name) {
        this.name = name;
    }

    public Account(String name, BigDecimal balance) {
        this.name = name;
        this.balance = balance;
    }
}
