package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ExistedReservationException extends CustomException {

    private static final String message = "예약이 존재합니다.";
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public ExistedReservationException() {
        super(message, status);
    }
}
