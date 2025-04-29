package roomescape.exception;

public class ReservationExistException extends RuntimeException {

    public ReservationExistException(final String message) {
        super(message);
    }
}
