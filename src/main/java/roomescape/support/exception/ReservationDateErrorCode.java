package roomescape.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReservationDateErrorCode implements ErrorCode {
    RESERVATION_DATE_NOT_EXIST(HttpStatus.NOT_FOUND,
        "존재하지 않는 날짜 입니다.", "날짜 목록 확인 후, 유효한 날짜를 선택해 주세요,"),
    RESERVATION_DATE_IN_USE(HttpStatus.CONFLICT,
        "이미 예약이 존재하는 날짜는 삭제할 수 없습니다.",
        "해당 날짜에 연결된 예약들을 먼저 취소하거나 변경한 뒤 다시 삭제해 주세요."),
    INVALID_RESERVATION_DATE(HttpStatus.BAD_REQUEST,
        "날짜는 비어 있을 수 없습니다.", "날짜를 입력해 주세요. (형식: yyyy-MM-dd"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String action;

    ReservationDateErrorCode(HttpStatus httpStatus, String message, String action) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.action = action;
    }

    @Override
    public String getCode() {
        return name();
    }
}
