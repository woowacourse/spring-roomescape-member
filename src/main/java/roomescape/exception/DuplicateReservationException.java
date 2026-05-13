package roomescape.exception;

import org.springframework.http.HttpStatus;

public class DuplicateReservationException extends RoomescapeBaseException {
    public DuplicateReservationException() {
        super(HttpStatus.CONFLICT, "해당 날짜·시간·테마에 이미 예약이 존재합니다.");
    }
}
