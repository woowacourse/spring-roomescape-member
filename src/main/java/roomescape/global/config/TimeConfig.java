package roomescape.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.global.time.SystemTimeManager;
import roomescape.global.time.TimeManager;

@Configuration
public class TimeConfig {

    @Bean
    public TimeManager timeProvider() {
        return new SystemTimeManager();
    }
}
