package roomescape.exception;

public class ReservationTimeExistException extends RuntimeException {
    public ReservationTimeExistException(String message) {
        super(message);
    }
}
