package roomescape.global.exception;

public class InvalidReservationPagingException extends RoomescapeException {

    public InvalidReservationPagingException(String message) {
        super(ErrorCode.INVALID_RESERVATION_PAGING, message);
    }
}
