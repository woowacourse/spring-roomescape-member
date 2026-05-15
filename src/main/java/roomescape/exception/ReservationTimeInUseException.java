package roomescape.exception;

public class ReservationTimeInUseException extends InUseException {
    public ReservationTimeInUseException(String message) {
        super(message);
    }
}
