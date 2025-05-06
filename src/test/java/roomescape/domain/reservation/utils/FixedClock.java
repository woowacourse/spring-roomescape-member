package roomescape.domain.reservation.utils;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class FixedClock {

    private FixedClock() {
    }

    public static Clock from(final LocalDateTime localDateTime) {
        final ZoneId zoneId = ZoneId.systemDefault();

        final Instant fixedInstant = localDateTime.atZone(zoneId)
                .toInstant();
        return Clock.fixed(fixedInstant, zoneId);
    }
}
