package roomescape.domain;


import lombok.Getter;
import roomescape.common.exception.DomainException;
import roomescape.common.exception.ErrorCode;

@Getter
public class ReservationTimeAvailability {
    private final ReservationTime reservationTime;
    private final boolean isAvailable;


    private ReservationTimeAvailability(ReservationTime reservationTime, boolean isAvailable) {
        validateReservationTime(reservationTime);
        this.reservationTime = reservationTime;
        this.isAvailable = isAvailable;
    }

    public static ReservationTimeAvailability available(ReservationTime reservationTime) {
        return new ReservationTimeAvailability(reservationTime, true);
    }

    public static ReservationTimeAvailability unavailable(ReservationTime reservationTime) {
        return new ReservationTimeAvailability(reservationTime, false);
    }

    private void validateReservationTime(ReservationTime reservationTime) {
        if (reservationTime == null) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_TIME);
        }
    }
}
