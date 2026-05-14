package roomescape.reservation.exception;

import java.time.LocalDateTime;

public class ReservationPastDateTimeException extends RuntimeException {

    public static final String MESSAGE = "과거로의 예약은 불가능합니다.";

    public ReservationPastDateTimeException(LocalDateTime now) {
        super(MESSAGE + " 현재시각=" + now);
    }

}
