package roomescape.exception;

import org.springframework.http.HttpStatus;

public class NotFoundReservationException extends CustomException {
    public NotFoundReservationException() {
        super("존재하지 않는 예약입니다.", HttpStatus.NOT_FOUND);
    }
}
