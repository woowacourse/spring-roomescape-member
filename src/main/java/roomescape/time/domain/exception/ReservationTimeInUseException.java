package roomescape.time.domain.exception;

public class ReservationTimeInUseException extends RuntimeException {
    public ReservationTimeInUseException(String message) {
        super(message);
    }
}
