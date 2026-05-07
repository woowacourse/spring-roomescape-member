package roomescape;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestClockConfig {

    @Bean
    public Clock clock() {
        return Clock.fixed(Instant.parse("2026-05-07T00:00:00Z"), ZoneId.systemDefault());
    }
}