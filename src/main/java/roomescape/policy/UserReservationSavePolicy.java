package roomescape.policy;

import roomescape.domain.Reservation;
import roomescape.exception.UnprocessableException;
import roomescape.exception.code.UnprocessableCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserReservationSavePolicy implements ReservationSavePolicy {

    @Override
    public void validate(Reservation reservation, LocalDateTime now) {
        LocalDate today = now.toLocalDate();
        if (reservation.isDateBefore(today)) {
            throw new UnprocessableException(UnprocessableCode.RESERVATION_PAST_DATE);
        }
        if (reservation.isDateTimeBefore(now)) {
            throw new UnprocessableException(UnprocessableCode.RESERVATION_PAST_TIME);
        }
    }
}
