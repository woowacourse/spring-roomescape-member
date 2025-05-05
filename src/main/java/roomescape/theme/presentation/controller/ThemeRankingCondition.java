package roomescape.theme.presentation.controller;

import static roomescape.theme.exception.InputErrorCode.INVALID_DATE_RANGE;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import roomescape.exception.InvalidInputException;

public record ThemeRankingCondition(LocalDate startDate, LocalDate endDate, int limit) {

    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LIMIT = 100;
    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");

    public static ThemeRankingCondition ofRequestParams(LocalDate requestStart, LocalDate requestEnd, Integer requestLimit) {
        LocalDate today = LocalDate.now(ZONE);

        DateRange dateRange = createDateRangeFrom(requestStart, requestEnd, today);
        int limit = calculateLimitFrom(requestLimit);

        return new ThemeRankingCondition(dateRange.start(), dateRange.end(), limit);
    }

    private record DateRange(LocalDate start, LocalDate end) {
    }

    private static DateRange createDateRangeFrom(LocalDate requestStart, LocalDate requestEnd, LocalDate today) {
        LocalDate start = Optional.ofNullable(requestStart)
                .orElse(today.minusDays(7));
        LocalDate end = Optional.ofNullable(requestEnd)
                .orElse(today.minusDays(1));

        validateDateRange(start, end);
        return new DateRange(start, end);
    }

    private static void validateDateRange(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new InvalidInputException(INVALID_DATE_RANGE);
        }
    }

    private static int calculateLimitFrom(Integer requestLimit) {
        return Optional.ofNullable(requestLimit)
                .filter(l -> l > 0)
                .map(l -> Math.min(l, MAX_LIMIT))
                .orElse(DEFAULT_LIMIT);
    }

}
