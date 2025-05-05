package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ReservationTimeNotFoundException extends CustomException {

    private static final String message = "예약시간이 존재하지 않습니다.";
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public ReservationTimeNotFoundException() {
        super(message, status);
    }
}
