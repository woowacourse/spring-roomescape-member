package roomescape.exception;

public class ReservationTimeNotFoundException extends RuntimeException {
    public ReservationTimeNotFoundException(String message) {
        super(message);
    }
}
