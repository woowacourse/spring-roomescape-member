package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public class ReservationDateException extends CustomException {
    public ReservationDateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
