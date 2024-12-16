package com.quickstarts.kitchensink.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class MongoTestingUtil {

    protected static final String MONGO_VERSION = "mongo:6.0";

    @Container
    protected static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.3-bionic"
    );

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.mongodb.database", () -> "testdb");
    }

    @BeforeAll
    public static void setUpAll() {
        mongoDBContainer.start();
    }

    @AfterAll
    public static void tearDownAll() {
        if (mongoDBContainer.isRunning()) {
            mongoDBContainer.stop();
        }
    }
}
