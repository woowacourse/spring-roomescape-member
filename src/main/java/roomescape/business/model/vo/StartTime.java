package roomescape.business.model.vo;

import roomescape.exception.business.InvalidCreateArgumentException;

import java.time.LocalTime;

import static roomescape.exception.ErrorCode.START_TIME_INVALID;

public record StartTime(
        LocalTime value
) {
    private static final LocalTime START_TIME = LocalTime.of(10, 0);
    private static final LocalTime END_TIME = LocalTime.of(23, 0);

    public StartTime {
        validateAvailableTime(value);
    }

    private static void validateAvailableTime(final LocalTime time) {
        if (time.isBefore(START_TIME) || time.isAfter(END_TIME)) {
            throw new InvalidCreateArgumentException(START_TIME_INVALID, START_TIME, END_TIME);
        }
    }

    public LocalTime plusMinutes(int minutes) {
        return value.plusMinutes(minutes);
    }

    public LocalTime minusMinutes(int minutes) {
        return plusMinutes(-minutes);
    }
}
