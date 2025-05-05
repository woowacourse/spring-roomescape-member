package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ReservationNotFoundException extends CustomException {

    private static final String message = "예약이 존재하지 않습니다.";
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public ReservationNotFoundException() {
        super(message, status);
    }
}
