package roomescape.common;

import java.time.Clock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class ClockConfig {

    @Bean
    @Primary
    public Clock testClock() {
        return Constant.FIXED_CLOCK;
    }
}
