package roomescape.admin.dto;

import roomescape.global.exception.RoomEscapeException;

import java.time.LocalDate;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static roomescape.global.exception.ExceptionMessage.*;

public record AdminReservationSaveRequest(
        LocalDate date,
        Long timeId,
        Long themeId,
        Long memberId
) {
    public AdminReservationSaveRequest {
        try {
            Objects.requireNonNull(date, DATE_CANNOT_NULL.getMessage());
            Objects.requireNonNull(timeId, TIME_CANNOT_NULL.getMessage());
            Objects.requireNonNull(themeId, THEME_CANNOT_NULL.getMessage());
            Objects.requireNonNull(memberId, MEMBER_CANNOT_NULL.getMessage());
        } catch (NullPointerException e) {
            throw new RoomEscapeException(BAD_REQUEST, e.getMessage());
        }
    }
}
