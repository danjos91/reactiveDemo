package io.github.danjos.reactivedemo.service;

import io.github.danjos.reactivedemo.model.Account;
import io.github.danjos.reactivedemo.repository.AccountR2dbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("integration-test") // Uses Testcontainers MySQL
class AccountServiceIntegrationTest {

    @Container
    static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.28");

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
    void testIntegrationWithRealMySQL() {
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
    void testComplexQueriesWithRealMySQL() {
        // Given
        var accounts = Flux.just(
                new Account("Rich1", BigDecimal.valueOf(50000)),
                new Account("Rich2", BigDecimal.valueOf(75000)),
                new Account("Poor1", BigDecimal.valueOf(5000)),
                new Account("Rich3", BigDecimal.valueOf(120000))
        );

        // When & Then
        StepVerifier.create(accountService.saveAll(accounts.collectList().block().toArray(new Account[0])))
                .expectNextCount(4)
                .verifyComplete();

        StepVerifier.create(accountService.findRichAccounts(BigDecimal.valueOf(10000)))
                .expectNextCount(3)
                .verifyComplete();
    }
} 