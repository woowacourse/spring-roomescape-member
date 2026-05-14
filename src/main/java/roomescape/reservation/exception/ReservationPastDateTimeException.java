package roomescape.reservation.exception;

import java.time.LocalDateTime;

public class ReservationPastDateTimeException extends RuntimeException {

    public static final String MESSAGE = "과거 예약은 생성, 변경, 취소할 수 없습니다.";

    public ReservationPastDateTimeException(LocalDateTime now) {
        super(MESSAGE + " 현재시각=" + now);
    }

}
