package roomescape.support;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestClockConfig {

    public static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");
    public static final LocalDate FIXED_DATE = LocalDate.of(2026, 5, 10);

    @Primary
    @Bean
    public Clock fixedClock() {
        return Clock.fixed(FIXED_DATE.atStartOfDay(ZONE_ID).toInstant(), ZONE_ID);
    }
}
