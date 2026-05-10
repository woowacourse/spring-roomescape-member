package roomescape.domain.reservation.dto;

import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.support.exception.BadRequestException;
import roomescape.support.exception.ReservationErrorCode;
import roomescape.support.exception.ReservationTimeErrorCode;
import roomescape.support.exception.ThemeErrorCode;

public record CreateReservationRequest(
    String name,
    Long dateId,
    Long timeId,
    Long themeId
) {

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new BadRequestException(ReservationErrorCode.INVALID_RESERVATION_NAME);
        }
        if (dateId == null) {
            throw new BadRequestException(ReservationErrorCode.INVALID_RESERVATION_DATE);
        }
        if (timeId == null) {
            throw new BadRequestException(ReservationTimeErrorCode.INVALID_RESERVATION_TIME);
        }
        if (themeId == null) {
            throw new BadRequestException(ThemeErrorCode.INVALID_THEME);
        }
    }

    public Reservation toEntity(ReservationDate reservationDate, ReservationTime reservationTime, Theme theme) {
        return Reservation.createWithoutId(
            name,
            reservationDate,
            reservationTime,
            theme
        );
    }
}
