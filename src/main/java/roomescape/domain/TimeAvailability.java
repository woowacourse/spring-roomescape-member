package roomescape.domain;

import java.time.LocalTime;

public record TimeAvailability(
        LocalTime time,
        boolean available
) {
}
