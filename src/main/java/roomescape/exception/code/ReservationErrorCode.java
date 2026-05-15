package roomescape.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReservationErrorCode implements ErrorCode {

    RESERVATION_DATE_TIME_EXPIRED(HttpStatus.UNPROCESSABLE_ENTITY, "지난 날짜와 시간으로는 예약할 수 없습니다."),
    DUPLICATE_RESERVATION(HttpStatus.CONFLICT, "해당 날짜와 시간, 테마에는 이미 예약이 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
