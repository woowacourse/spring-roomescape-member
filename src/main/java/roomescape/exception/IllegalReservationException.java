package roomescape.exception;

public class IllegalReservationException extends IllegalRequestException {
    public IllegalReservationException(String message) {
        super(message);
    }

    public IllegalReservationException(String message, Throwable cause) {
        super(message, cause);
    }
}
