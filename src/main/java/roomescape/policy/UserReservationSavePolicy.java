package roomescape.policy;

import roomescape.command.ReservationSaveCommand;
import roomescape.domain.ReservationTime;
import roomescape.exception.ErrorCode;
import roomescape.exception.UnprocessableException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserReservationSavePolicy implements ReservationSavePolicy {

    @Override
    public void validate(ReservationSaveCommand command, ReservationTime reservationTime, LocalDateTime now) {
        LocalDate today = now.toLocalDate();
        if (command.date().isBefore(today)) {
            throw new UnprocessableException(ErrorCode.RESERVATION_PAST_DATE);
        }
        if (command.date().isEqual(today) && reservationTime.isBefore(now.toLocalTime())) {
            throw new UnprocessableException(ErrorCode.RESERVATION_PAST_TIME);
        }
    }
}
