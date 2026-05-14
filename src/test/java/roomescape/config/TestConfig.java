package roomescape.config;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {

    @Bean
    @Primary
    public Clock clock() {
        return Clock.fixed(
                Instant.parse("2026-04-28T09:00:00Z"),
                ZoneOffset.UTC
        );
    }
}
