package roomescape.exception;

import org.springframework.http.HttpStatus;

public class PastReservationControlException extends RoomescapeException {
    public PastReservationControlException() {
        super("이미 지난 예약은 수정/삭제할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
