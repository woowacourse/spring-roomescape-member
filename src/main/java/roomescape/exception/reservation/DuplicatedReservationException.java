package roomescape.exception.reservation;

import org.springframework.http.HttpStatus;
import roomescape.exception.RoomescapeException;

public class DuplicatedReservationException extends RoomescapeException {
    public DuplicatedReservationException() {
        super("중복된 예약입니다.", HttpStatus.CONFLICT);
    }
}
