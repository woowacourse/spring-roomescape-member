package roomescape.domain.reservation.dto;

import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.support.exception.ReservationErrorCode;
import roomescape.support.exception.ReservationTimeErrorCode;
import roomescape.support.exception.RoomescapeException;

public record CreateReservationRequest(
    String name,
    Long dateId,
    Long timeId
) {

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new RoomescapeException(ReservationErrorCode.INVALID_RESERVATION_NAME);
        }
        if (dateId == null) {
            throw new RoomescapeException(ReservationErrorCode.INVALID_RESERVATION_DATE);
        }
        if (timeId == null) {
            throw new RoomescapeException(ReservationTimeErrorCode.INVALID_RESERVATION_TIME);
        }
    }

    public Reservation toEntity(ReservationDate reservationDate, ReservationTime reservationTime) {
        return Reservation.createWithoutId(
            name,
            reservationDate,
            reservationTime
        );
    }
}
