package roomescape.common.config;

import java.time.Clock;
import org.springframework.stereotype.Component;

@Component
public class ClockProvider {

    public Clock getClock() {
        return Clock.systemDefaultZone();
    }
}
