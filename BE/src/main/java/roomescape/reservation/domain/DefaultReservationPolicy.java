package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import roomescape.global.exception.ReservationErrorCode;
import roomescape.global.exception.customException.BusinessException;
import roomescape.reservationTime.domain.ReservationTime;

@Component public class DefaultReservationPolicy implements ReservationPolicy {

    @Override
    public void pastDateTime(LocalDate date, ReservationTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new BusinessException(ReservationErrorCode.RESERVATION_CREATE_IN_PAST);
        }
    }
}
