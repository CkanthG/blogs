package com.blog.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

public class AbstractTestContainer {

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withUsername("postgres")
            .withPassword("password")
            .withDatabaseName("blogs_db")
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("migration.sql"),
                    "/docker-entrypoint-initdb.d/"
            ).withReuse(true);

    static {
        postgreSQLContainer.start();
        var mappedPort = postgreSQLContainer.getMappedPort(5432).toString();
        System.out.println("mappedPort @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + mappedPort);
        System.setProperty("POSTGRES_PORT", mappedPort);
        System.out.println("Get System POSTGRES_PORT : " + System.getProperty("POSTGRES_PORT"));
        // Optionally, if you want the container to be reused across tests (faster initialization)
        postgreSQLContainer.withReuse(true);
    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        // PostgreSQL properties
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword());
    }

}
