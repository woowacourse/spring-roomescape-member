package roomescape.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    CANNOT_SELECT_PAST_DATETIME("지나간 날짜, 시간에 대한 예약 생성은 불가능합니다."),
    INVALID_DATA_FORMAT("요청 데이터의 형식이 올바르지 않습니다."),
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

}
