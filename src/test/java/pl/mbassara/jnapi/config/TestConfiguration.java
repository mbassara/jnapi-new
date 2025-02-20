package pl.mbassara.jnapi.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            // Prevent spring from invoking the pl.mbassara.jnapi.application.ApplicationRunner
        };
    }
}
