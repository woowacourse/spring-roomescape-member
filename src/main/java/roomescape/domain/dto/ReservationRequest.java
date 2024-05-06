package roomescape.domain.dto;

import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.TimeSlot;
import roomescape.exception.ErrorType;
import roomescape.exception.InvalidClientRequestException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ReservationRequest {
        isValid(name, date, timeId, themeId);
    }

    public Reservation toEntity(final Long id, final TimeSlot time, final Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }

    private void isValid(final String name, final LocalDate date, final Long timeId, final Long themeId) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidClientRequestException(ErrorType.EMPTY_VALUE_NOT_ALLOWED, "name", name);
        }

        if (date == null) {
            throw new InvalidClientRequestException(ErrorType.EMPTY_VALUE_NOT_ALLOWED, "date", "");
        }

        if (timeId == null) {
            throw new InvalidClientRequestException(ErrorType.EMPTY_VALUE_NOT_ALLOWED, "timeId", "");
        }

        if (timeId <= 0) {
            throw new InvalidClientRequestException(ErrorType.INVALID_TIME, "timeId", timeId.toString());
        }

        if (themeId == null) {
            throw new InvalidClientRequestException(ErrorType.EMPTY_VALUE_NOT_ALLOWED, "themeId", "");
        }

        if (themeId <= 0) {
            throw new InvalidClientRequestException(ErrorType.INVALID_THEME, "themeId", themeId.toString());
        }

        if (date.isBefore(LocalDate.now())) {
            throw new InvalidClientRequestException(ErrorType.PAST_DATE_NOT_ALLOWED, "date", date.format(DATE_TIME_FORMAT));
        }
    }
}
