package roomescape.common;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // TODO : Test Configuration과 분리
    @Bean
    public Clock clock() {
        return Clock.fixed(
                LocalDate.of(2025, 4, 30).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
    }
}
