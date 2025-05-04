package roomescape.reservation.domain.exception;

public class ImpossibleReservationException extends RuntimeException {
    public ImpossibleReservationException(String message) {
        super(message);
    }
}
