package roomescape.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedReservationTimeException extends CustomException {
    public DuplicatedReservationTimeException() {
        super("중복된 예약시간입니다.", HttpStatus.CONFLICT);
    }
}
