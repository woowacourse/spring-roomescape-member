package roomescape.domain.reservation.error.exception;

import org.springframework.http.HttpStatus;
import roomescape.domain.reservation.error.type.ReservationErrorType;

public class ReservationException extends RuntimeException {

    private final HttpStatus status;

    public ReservationException(ReservationErrorType errorType) {
        super(errorType.message());
        this.status = errorType.status();
    }

    public HttpStatus getStatus() {
        return status;
    }
}
