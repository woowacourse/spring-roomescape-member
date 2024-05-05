package roomescape.exception;

import org.springframework.http.HttpStatusCode;

public enum CustomExceptionCode {

    INPUT_NULL_IS_NOT_ALLOWED(400, "null은 입력할 수 없습니다."),
    INPUT_BLANK_IS_NOT_ALLOWED(400, "빈 문자열은 입력할 수 없습니다."),
    RESERVATION_FOR_PAST_IS_NOT_ALLOWED(400, "지나간 시간에 대한 예약은 할 수 없습니다."),
    RESERVATION_ALREADY_EXIST(400, "예약이 이미 존재합니다."),
    CAN_NOT_DELETE_THEME_CAUSE_RESERVATION_EXIST(400, "해당 테마에 예약이 존재하기 때문에 삭제할 수 없습니다."),
    CAN_NOT_DELETE_TIME_CAUSE_RESERVATION_EXIST(400, "해당 시간에 예약이 존재하기 때문에 삭제할 수 없습니다."),

    ;

    CustomExceptionCode(int code, String message) {
        this.httpStatusCode = HttpStatusCode.valueOf(code);
        this.detail = message;
    }

    private final HttpStatusCode httpStatusCode;
    private final String detail;

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getDetail() {
        return detail;
    }
}
