package roomescape.reservationtime.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class UsingReservationTimeException extends RoomescapeException {
    public UsingReservationTimeException() {
        super(HttpStatus.BAD_REQUEST, TimeErrorCode.USING_TIME);
    }
}
