package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class AlreadyCanceledReservationException extends CustomException {

    public AlreadyCanceledReservationException(String message) {
        super("ALREADY_CANCELED_RESERVATION", HttpStatus.UNPROCESSABLE_ENTITY, message);
    }
}
