package roomescape.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.global.time.SystemTimeProvider;
import roomescape.global.time.TimeProvider;

@Configuration
public class TimeConfig {

    @Bean
    public TimeProvider timeProvider() {
        return new SystemTimeProvider();
    }
}
