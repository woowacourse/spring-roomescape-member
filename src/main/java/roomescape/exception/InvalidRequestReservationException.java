package roomescape.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestReservationException extends CustomException {
    public InvalidRequestReservationException() {
        super("형식에 맞지 않는 예약입니다", HttpStatus.BAD_REQUEST);
    }
}
