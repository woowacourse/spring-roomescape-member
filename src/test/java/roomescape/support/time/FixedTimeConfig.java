package roomescape.support.time;

import java.time.LocalDateTime;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import roomescape.global.time.TimeProvider;

@TestConfiguration
public class FixedTimeConfig {

    @Bean
    @Primary
    public TimeProvider testTimeProvider() {
        return new FixedTimeProvider(
                LocalDateTime.of(2026, 5, 16, 13, 0)
        );
    }
}
