package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class AlreadyPassedReservationException extends CustomException {

    public AlreadyPassedReservationException(String message) {
        super("ALREADY_PASSED_RESERVATION", HttpStatus.UNPROCESSABLE_ENTITY, message);
    }
}
