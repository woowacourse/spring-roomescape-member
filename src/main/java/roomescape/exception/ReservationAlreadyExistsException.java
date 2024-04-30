package roomescape.exception;

public class ReservationAlreadyExistsException extends IllegalArgumentException {

    public ReservationAlreadyExistsException(String message) {
        super(message);
    }

}
