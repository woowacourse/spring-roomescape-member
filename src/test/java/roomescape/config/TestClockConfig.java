package roomescape.config;

import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import roomescape.domain.reservation.utils.FixedClock;

@TestConfiguration
public class TestClockConfig {

    @Bean
    public Clock clock() {
        return FixedClock.from(LocalDateTime.of(2024, 12, 18, 8, 0));
    }

}
