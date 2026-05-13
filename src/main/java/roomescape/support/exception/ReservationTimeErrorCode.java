package roomescape.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReservationTimeErrorCode implements ErrorCode {

    INVALID_RESERVATION_TIME(HttpStatus.BAD_REQUEST,
        "시간은 필수입니다.", "방문하실 시간을 선택해 주세요."),
    RESERVATION_TIME_NOT_EXIST(HttpStatus.NOT_FOUND,
        "존재하지 않는 예약 시간대 입니다.", "시간 목록 확인 후, 유효한 시간을 선택해 주세요"),
    RESERVATION_TIME_IN_USE(HttpStatus.CONFLICT,
        "이미 예약이 존재하는 시간대는 삭제할 수 없습니다.",
        "해당 시간에 연결된 예약들을 먼저 취소하거나 변경한 뒤 다시 삭제해 주세요."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String action;

    ReservationTimeErrorCode(HttpStatus httpStatus, String message, String action) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.action = action;
    }

    @Override
    public String getCode() {
        return name();
    }
}
