package roomescape.exception;

public class IllegalReservationException extends IllegalArgumentException { // TODO: IllegalArgumentException가 최선일까?

    public IllegalReservationException(final String message) {
        super(message);
    }
}
