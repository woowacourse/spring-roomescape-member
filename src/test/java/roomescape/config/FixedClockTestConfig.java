package roomescape.config;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class FixedClockTestConfig {

    @Bean
    @Primary
    public Clock fixedClock() {
        return Clock.fixed(
                LocalDate.of(2026, 5, 8)
                        .atStartOfDay(ZoneId.of("Asia/Seoul"))
                        .toInstant(),
                ZoneId.of("Asia/Seoul")
        );
    }

}
