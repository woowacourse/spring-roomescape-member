package roomescape.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Configuration
public class FixedClockConfig {

    @Bean
    @Primary
    public Clock clock1() {
        return Clock.fixed(
                Instant.parse("2026-05-10T03:00:00Z"),
                ZoneId.of("Asia/Seoul")
        );
    }
}
