package roomescape.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReservationTimeErrorCode implements ErrorCode {
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID를 갖는 시간대는 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
