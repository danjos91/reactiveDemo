package io.github.danjos.reactivedemo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public abstract class ReactiveDemoApplicationTests {

    @Container // декларируем объект учитываемым тест-контейнером
    @ServiceConnection // автоматически назначаем параметры соединения с контейнером
    static final MySQLContainer<?> mysqlContainer =
            new MySQLContainer<>("mysql:8.0.28");

}

