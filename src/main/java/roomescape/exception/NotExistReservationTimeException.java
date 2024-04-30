package roomescape.exception;

public class NotExistReservationTimeException extends IllegalArgumentException {

    public NotExistReservationTimeException(String message) {
        super(message);
    }
}
