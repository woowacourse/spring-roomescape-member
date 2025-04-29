package roomescape.utils;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class FixedClock {

    private FixedClock() {
    }

    public static Clock from(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();

        Instant fixedInstant = localDateTime.atZone(zoneId).toInstant();
        return Clock.fixed(fixedInstant, zoneId);
    }
}
