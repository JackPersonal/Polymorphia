package polymorphia.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication
public class PolymorphiaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolymorphiaServerApplication.class, args);
    }

    @Bean
    public ApplicationRunner printEndpoints(
            @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping mapping) {
        Logger log = LoggerFactory.getLogger("Endpoints");
        return args -> mapping.getHandlerMethods().forEach((info, method) -> {
            String pattern = info.getPatternValues().stream().findFirst().orElse("");
            if (pattern.startsWith("/api/")) {
                log.info("{} -> {}", info, method);
            }
        });
    }
}