package roomescape.exception;

public class ExistReservationInReservationTimeException extends IllegalArgumentException{

    public ExistReservationInReservationTimeException(String message) {
        super(message);
    }
}
