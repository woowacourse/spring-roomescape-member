package roomescape.global.exception;

public class DuplicateReservationException extends RuntimeException{
    public DuplicateReservationException(String message) {
        super(message);
    }
}
