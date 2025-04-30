package roomescape.common.exception;

public class NotFoundReservationException extends NotFoundException{
    public NotFoundReservationException() {
    }

    public NotFoundReservationException(String message) {
        super(message);
    }
}
