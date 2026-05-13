package roomescape.exception.business;

import org.springframework.http.HttpStatus;

public class PastTimeReservationException extends BusinessException {

    public PastTimeReservationException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}