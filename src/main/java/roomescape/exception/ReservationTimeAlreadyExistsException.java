package roomescape.exception;

public class ReservationTimeAlreadyExistsException extends IllegalArgumentException {

    public ReservationTimeAlreadyExistsException(String format) {
        super(format);
    }
}
