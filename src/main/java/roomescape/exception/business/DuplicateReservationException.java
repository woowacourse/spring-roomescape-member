package roomescape.exception.business;

import org.springframework.http.HttpStatus;

public class DuplicateReservationException extends BusinessException {

    public DuplicateReservationException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}