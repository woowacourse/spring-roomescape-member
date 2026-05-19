package roomescape.domain;

import roomescape.exception.BadRequestException;
import roomescape.exception.code.BadRequestCode;

import java.time.Clock;
import java.time.LocalDate;

public record Period(LocalDate startInclusive, LocalDate endInclusive) {

    private static final int POPULAR_START_DAYS_AGO = 7;
    private static final int POPULAR_END_DAYS_AGO = 1;

    public Period {
        if (startInclusive == null || endInclusive == null) {
            throw new BadRequestException(BadRequestCode.INVALID_PERIOD);
        }
        if (endInclusive.isBefore(startInclusive)) {
            throw new BadRequestException(BadRequestCode.INVALID_PERIOD);
        }
    }

    public static Period lastWeek(Clock clock) {
        LocalDate today = LocalDate.now(clock);
        return new Period(
                today.minusDays(POPULAR_START_DAYS_AGO),
                today.minusDays(POPULAR_END_DAYS_AGO)
        );
    }
}
