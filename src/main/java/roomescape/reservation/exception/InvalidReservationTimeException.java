package roomescape.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidReservationTimeException extends RuntimeException {

    public InvalidReservationTimeException(Long timeId) {
        super("존재하지 않는 예약 시간입니다. timeId=" + timeId);
    }
}
