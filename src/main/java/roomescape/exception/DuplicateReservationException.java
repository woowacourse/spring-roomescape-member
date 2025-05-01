package roomescape.exception;

import java.time.LocalDate;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Theme;

public class DuplicateReservationException extends IllegalStateException {

    private static final String DEFAULT_MESSAGE_FORMAT = "해당 날짜와 시간에 이미 존재하는 예약 있습니다. Date : %s, Time : %s, Theme : %s";

    public DuplicateReservationException(final String message) {
        super(message);
    }

    public DuplicateReservationException(
            final LocalDate date,
            final PlayTime playTime,
            final Theme theme
    ) {
        super(DEFAULT_MESSAGE_FORMAT.formatted(date, playTime.getStartAt(), theme.getName()));
    }
}
