package roomescape.config;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 개발 중 시간 관련 로직을 고정된 시점으로 다루기 위한 Clock 설정.
 */
@Configuration
public class ClockConfig {

    @Bean
    public Clock fixedClock() {
        return Clock.fixed(
                LocalDate.of(2025, 4, 30).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
    }
}
