package roomescape.reservation.domain.exception;

public class ReservationOwnerMismatchException extends RuntimeException {

    public ReservationOwnerMismatchException(String message) {
        super(message);
    }
}
