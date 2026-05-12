package roomescape.reservation.domain.exception;

public class UnauthorizedReservationChangeException extends RuntimeException {
    public UnauthorizedReservationChangeException(String message) {
        super(message);
    }
}
