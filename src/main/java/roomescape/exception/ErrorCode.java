package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_REQUEST_FORMAT(HttpStatus.BAD_REQUEST, "JSON 파싱 실패 혹은 URL 경로 변수 타입 오류가 발생했습니다"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "필수값이 누락되었거나 필드 유효성 검증에 실패했습니다"),
    INVALID_REQUEST_URI_VARIABLE_TYPE(HttpStatus.BAD_REQUEST, "요청 URI 형식이 올바르지 않습니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 경로입니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
    DUPLICATED_RESERVATION(HttpStatus.CONFLICT, "같은 날짜, 시간, 테마 의 예약이 이미 존재합니다"),
    ;

    private final HttpStatus code;
    private final String message;

    ErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

    public HttpStatus getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
