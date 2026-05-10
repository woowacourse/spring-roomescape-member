package roomescape;

import java.time.LocalDate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import roomescape.service.TimeProvider;

@Configuration
public class TestClockConfig {

    @Bean
    @Primary
    public TimeProvider timeProvider() {
        return () -> LocalDate.of(2026, 5, 7);
    }
}