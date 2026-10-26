package polymorphia;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

class LoggingSanityTest {
    @Test
    void showLoggerBackend() {
        System.out.println("\nLogging Factory: " + LoggerFactory.getILoggerFactory().getClass().getName() + "\n");
    }
}
