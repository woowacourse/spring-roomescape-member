package roomescape;

import java.time.LocalDateTime;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import roomescape.service.nowdate.CurrentDateTime;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public CurrentDateTime currentDate() {
        return new CurrentATime();
    }

    static class CurrentATime implements CurrentDateTime {

        @Override
        public LocalDateTime get() {
            return LocalDateTime.of(2025, 1, 1, 10, 30);
        }
    }
}
