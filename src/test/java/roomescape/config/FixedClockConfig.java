package roomescape.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@TestConfiguration
public class FixedClockConfig {

    @Bean
    @Primary
    public Clock clock() {
        return Clock.fixed(
                Instant.parse("2026-05-10T03:00:00Z"),
                ZoneId.of("Asia/Seoul")
        );
    }
}
