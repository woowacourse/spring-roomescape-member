package roomescape.common.exception;

public class ReservationTimeValidationException extends DomainValidationException {
    public ReservationTimeValidationException() {
    }

    public ReservationTimeValidationException(String message) {
        super(message);
    }
}
