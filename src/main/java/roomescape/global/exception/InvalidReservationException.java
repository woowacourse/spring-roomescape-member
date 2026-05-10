package roomescape.global.exception;

public class InvalidReservationException extends RoomescapeException {

    public InvalidReservationException(String message) {
        super(ErrorCode.INVALID_RESERVATION, message);
    }
}
