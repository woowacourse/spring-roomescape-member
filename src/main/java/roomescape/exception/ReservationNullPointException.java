package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ReservationNullPointException extends CustomException {

    private static final String message = "값이 존재하지 않습니다.";
    private static final HttpStatus status = HttpStatus.NOT_FOUND;

    public ReservationNullPointException() {
        super(message, status);
    }
}
