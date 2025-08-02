package io.github.danjos.reactivedemo.repository;

import io.github.danjos.reactivedemo.model.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

@Repository
public interface AccountR2dbcRepository extends ReactiveCrudRepository<Account, Integer> {
    // возвращаем все аккаунты с балансом больше amount
    Flux<Account> findAccountsByBalanceGreaterThan(BigDecimal amount);
}
