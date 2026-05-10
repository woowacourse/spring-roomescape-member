package roomescape.global.exception;

public class DuplicateReservationException extends RoomescapeException {

    public DuplicateReservationException() {
        super(ErrorCode.DUPLICATE_RESERVATION, "이미 예약된 시간입니다.");
    }
}
