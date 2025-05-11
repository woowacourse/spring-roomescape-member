package roomescape.common.exception;

public class ReservationValidationException extends DomainValidationException {
    public ReservationValidationException() {
    }

    public ReservationValidationException(String message) {
        super(message);
    }
}
