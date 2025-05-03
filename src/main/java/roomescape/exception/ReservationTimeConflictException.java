package roomescape.exception;

public class ReservationTimeConflictException extends RuntimeException {
    public ReservationTimeConflictException(String message) {
        super(message);
    }
}
