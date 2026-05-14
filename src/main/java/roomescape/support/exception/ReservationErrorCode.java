package roomescape.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReservationErrorCode implements ErrorCode {
    INVALID_RESERVATION_NAME(HttpStatus.BAD_REQUEST,
        "이름은 비어 있을 수 없습니다.", "예약자 이름을 입력해 주세요."),
    INVALID_RESERVATION_DATE(HttpStatus.BAD_REQUEST,
        "날짜는 필수입니다.", "방문하실 날짜를 선택해 주세요."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND,
        "존재하지 않는 예약건 입니다.", "예약 번호가 정확한지 확인해 주세요."),
    RESERVATION_CANNOT_CANCEL(HttpStatus.BAD_REQUEST,
        "당일 및 지난 예약은 취소할 수 없습니다.",
        "취소는 방문 전날 자정까지만 가능합니다. 도움이 필요하시면 고객센터로 문의해 주세요."),
    RESERVATION_DUPLICATED(HttpStatus.CONFLICT,
        "이미 예약이 완료된 시간대입니다.", "다른 시간대나 다른 테마를 선택하여 다시 시도해 주세요.");

    private final HttpStatus httpStatus;
    private final String message;
    private final String action;

    ReservationErrorCode(HttpStatus httpStatus, String message, String action) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.action = action;
    }

    @Override
    public String getCode() {
        return name();
    }
}
