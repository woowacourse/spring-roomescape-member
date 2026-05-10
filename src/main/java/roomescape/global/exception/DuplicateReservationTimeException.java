package roomescape.global.exception;

public class DuplicateReservationTimeException extends RoomescapeException {

    public DuplicateReservationTimeException() {
        super(ErrorCode.DUPLICATE_RESERVATION_TIME, "이미 등록된 예약 시간입니다.");
    }
}
