package roomescape.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedReservationException extends CustomException {
    public DuplicatedReservationException() {
        super("중복된 예약입니다.", HttpStatus.CONFLICT);
    }
}
