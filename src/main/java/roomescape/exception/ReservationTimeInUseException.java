package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ReservationTimeInUseException extends RoomescapeBaseException {
    public ReservationTimeInUseException() {
        super(HttpStatus.CONFLICT, "예약이 존재하는 시간은 삭제할 수 없습니다.");
    }
}
