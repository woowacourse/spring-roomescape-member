package roomescape.common.exception;

public class NotFoundReservationTimeException extends NotFoundException {
    public NotFoundReservationTimeException() {
    }

    public NotFoundReservationTimeException(String message) {
        super(message);
    }
}
