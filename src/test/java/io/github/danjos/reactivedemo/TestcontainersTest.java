package io.github.danjos.reactivedemo;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class TestcontainersTest {

    @Container
    static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.28");

    @Test
    void testContainerIsRunning() {
        // Verify the container is running
        assertThat(mysqlContainer.isRunning()).isTrue();
        
        // Verify we can get connection info
        assertThat(mysqlContainer.getJdbcUrl()).isNotNull();
        assertThat(mysqlContainer.getUsername()).isEqualTo("test");
        assertThat(mysqlContainer.getPassword()).isEqualTo("test");
        
        System.out.println("✅ Testcontainers MySQL container is working!");
        System.out.println("Database URL: " + mysqlContainer.getJdbcUrl());
        System.out.println("Username: " + mysqlContainer.getUsername());
        System.out.println("Password: " + mysqlContainer.getPassword());
    }

    @Test
    void testContainerConnection() {
        // Test that we can connect to the database
        assertThat(mysqlContainer.isRunning()).isTrue();
        
        // Try to execute a simple query
        try (var connection = mysqlContainer.createConnection("")) {
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("SELECT 1");
            assertThat(resultSet.next()).isTrue();
            assertThat(resultSet.getInt(1)).isEqualTo(1);
            
            System.out.println("✅ Database connection test passed!");
        } catch (Exception e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
} 