package roomescape.support;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

@TestConfiguration
public class FixedClockConfig {

    public static final LocalDateTime FIXED_NOW = LocalDateTime.of(2026, 5, 6, 9, 0);

    @Bean
    @Primary
    public Clock fixedClock() {
        return Clock.fixed(
                FIXED_NOW.atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
    }
}
