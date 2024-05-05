package roomescape.exception;

public class ReservingPastTimeException extends IllegalArgumentException {

    public ReservingPastTimeException(String message) {
        super(message);
    }
}
