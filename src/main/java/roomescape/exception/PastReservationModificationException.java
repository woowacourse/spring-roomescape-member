package roomescape.exception;

import org.springframework.http.HttpStatus;

public class PastReservationModificationException extends RoomescapeBaseException {
    public PastReservationModificationException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "이미 지난 예약은 수정할 수 없습니다.");
    }
}