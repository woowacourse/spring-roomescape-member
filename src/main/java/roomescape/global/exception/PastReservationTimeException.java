package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class PastReservationTimeException extends CustomException {

    public PastReservationTimeException(String message) {
        super("PAST_RESERVATION_TIME", HttpStatus.UNPROCESSABLE_ENTITY, message);
    }
}
