package roomescape.common;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Constant {

    private Constant() {}

    private static final LocalDateTime FIXED_DATE_TIME = LocalDateTime.of(2025, 4, 20, 10, 0);
    private static final ZoneId TIME_ZONE = ZoneId.of("Asia/Seoul");
    private static final Instant FIXED_INSTANT = FIXED_DATE_TIME.atZone(TIME_ZONE).toInstant();
    public static final Clock FIXED_CLOCK = Clock.fixed(FIXED_INSTANT, TIME_ZONE);
}
