package roomescape.exception;

import org.springframework.http.HttpStatus;

public class InvalidReservationDateTimeException extends RoomescapeBaseException {
    public InvalidReservationDateTimeException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "예약 일정이 유효하지 않습니다. 예약 날짜와 시간은 현시간 이후여야 합니다.");
    }
}
