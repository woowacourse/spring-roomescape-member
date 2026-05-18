package roomescape.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReservationErrorCode implements ErrorCode {

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID를 갖는 예약은 존재하지 않습니다."),
    RESERVATION_DATE_TIME_EXPIRED(HttpStatus.UNPROCESSABLE_ENTITY, "지난 날짜와 시간으로는 예약할 수 없습니다."),
    DUPLICATE_RESERVATION(HttpStatus.CONFLICT, "해당 날짜와 시간, 테마에는 이미 예약이 존재합니다."),
    RESERVATION_TIME_DELETE_CONFLICT(HttpStatus.CONFLICT, "이미 예약에 사용 중인 시간대는 삭제할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
