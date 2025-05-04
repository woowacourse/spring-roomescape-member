package roomescape.common.exception;

public class DeleteReservationException extends InvalidRequestException {
    public DeleteReservationException() {
    }

    public DeleteReservationException(String message) {
        super(message);
    }
}
