package roomescape.global.exception;

public class ReservationNotFoundException extends RoomescapeException {

    public ReservationNotFoundException() {
        super(ErrorCode.RESERVATION_NOT_FOUND, "존재하지 않는 예약 번호입니다.");
    }
}
