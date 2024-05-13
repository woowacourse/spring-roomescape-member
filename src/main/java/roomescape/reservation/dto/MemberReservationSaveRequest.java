package roomescape.reservation.dto;

import roomescape.admin.dto.ReservationSaveRequest;
import roomescape.global.auth.AuthUser;
import roomescape.global.exception.RoomEscapeException;

import java.time.LocalDate;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static roomescape.global.exception.ExceptionMessage.*;

public record MemberReservationSaveRequest(
        LocalDate date,
        Long timeId,
        Long themeId
) {
    public MemberReservationSaveRequest {
        try {
            Objects.requireNonNull(date, DATE_CANNOT_NULL.getMessage());
            Objects.requireNonNull(timeId, TIME_CANNOT_NULL.getMessage());
            Objects.requireNonNull(themeId, THEME_CANNOT_NULL.getMessage());
        } catch (NullPointerException e) {
            throw new RoomEscapeException(BAD_REQUEST, e.getMessage());
        }
    }

    public ReservationSaveRequest generateReservationSaveRequest(AuthUser authUser) {
        return new ReservationSaveRequest(date, timeId, themeId, authUser.getId());
    }
}
