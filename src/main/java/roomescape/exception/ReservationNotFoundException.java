package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ReservationNotFoundException extends RoomescapeException {

    public ReservationNotFoundException(long id) {
        super("해당 식별자의 예약을 찾을 수 없습니다. id: " + id, HttpStatus.NOT_FOUND);
    }
}