package roomescape.domain.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.stereotype.Component;
import roomescape.common.exception.InvalidReservationException;

@Component
public class ReservationValidator {

    public void validateFutureReservationDateTime(LocalDate date, LocalTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);

        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidReservationException("지난 날짜와 시간으로는 예약할 수 없습니다.");
        }
    }
}
