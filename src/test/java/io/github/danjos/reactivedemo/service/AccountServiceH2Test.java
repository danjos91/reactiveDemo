package io.github.danjos.reactivedemo.service;

import io.github.danjos.reactivedemo.model.Account;
import io.github.danjos.reactivedemo.repository.AccountR2dbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test") // Uses H2 database
class AccountServiceH2Test {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountR2dbcRepository accountRepository;

    @BeforeEach
    void setUp() {
        // Clean up before each test
        accountRepository.deleteAll().block();
    }

    @Test
    void testSaveAndFindRichAccounts() {
        // Given
        var richAccount = new Account("Lagertha", BigDecimal.valueOf(100000));
        var poorAccount = new Account("Ragnar", BigDecimal.ZERO);

        // When
        Flux<Account> savedAccounts = accountService.saveAll(richAccount, poorAccount);
        Flux<Account> richAccounts = accountService.findRichAccounts(BigDecimal.TEN);

        // Then
        StepVerifier.create(savedAccounts)
                .expectNextCount(2)
                .verifyComplete();

        StepVerifier.create(richAccounts)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testFindRichAccountsWithH2() {
        // Given
        var account1 = new Account("Rich1", BigDecimal.valueOf(50000));
        var account2 = new Account("Rich2", BigDecimal.valueOf(75000));
        var account3 = new Account("Poor1", BigDecimal.valueOf(5000));

        // When & Then
        StepVerifier.create(accountService.saveAll(account1, account2, account3))
                .expectNextCount(3)
                .verifyComplete();

        StepVerifier.create(accountService.findRichAccounts(BigDecimal.valueOf(10000)))
                .expectNextCount(2)
                .verifyComplete();
    }
} 