package roomescape.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.ZoneId;

@Component
public class ClockConfig {

    @Bean
    public Clock clock () {
        return Clock.system(ZoneId.of("Asia/Seoul"));
    }
}
