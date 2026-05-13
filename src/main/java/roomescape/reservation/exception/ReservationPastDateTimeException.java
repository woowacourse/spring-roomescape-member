package roomescape.reservation.exception;

import java.time.LocalDateTime;

public class ReservationPastDateTimeException extends RuntimeException {

    public ReservationPastDateTimeException(LocalDateTime now) {
        super("과거로의 예약은 불가능합니다. 현재시각=" + now);
    }

}
