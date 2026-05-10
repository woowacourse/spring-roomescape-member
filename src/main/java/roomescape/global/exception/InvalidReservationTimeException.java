package roomescape.global.exception;

public class InvalidReservationTimeException extends RoomescapeException {

    public InvalidReservationTimeException(String message) {
        super(ErrorCode.INVALID_RESERVATION_TIME, message);
    }
}
