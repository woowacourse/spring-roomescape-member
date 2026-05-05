package roomescape.reservation.domain.exception;

public class ReservationInUseException extends RuntimeException {
    public ReservationInUseException(String message) {
        super(message);
    }
}
