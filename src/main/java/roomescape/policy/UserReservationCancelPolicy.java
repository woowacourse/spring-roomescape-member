package roomescape.policy;

import roomescape.domain.Reservation;
import roomescape.exception.UnprocessableException;
import roomescape.exception.code.UnprocessableCode;

import java.time.LocalDateTime;

public class UserReservationCancelPolicy implements ReservationCancelPolicy {

    @Override
    public void validate(Reservation reservation, LocalDateTime now) {
        if (reservation.isDateTimeBefore(now)) {
            throw new UnprocessableException(UnprocessableCode.RESERVATION_ALREADY_STARTED);
        }
    }
}
