package roomescape.config;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class FixedClockTestConfig {

    @Bean
    public Clock clock() {
        return Clock.fixed(
                LocalDate.of(2026, 5, 8)
                        .atStartOfDay(ZoneId.of("Asia/Seoul"))
                        .toInstant(),
                ZoneId.of("Asia/Seoul")
        );
    }

}
