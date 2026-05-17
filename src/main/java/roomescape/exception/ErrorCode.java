package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_REQUEST_FORMAT(HttpStatus.BAD_REQUEST, "JSON 파싱 실패 혹은 URL 경로 변수 타입 오류가 발생했습니다"),
    INVALID_REQUEST_URI_VARIABLE_TYPE(HttpStatus.BAD_REQUEST, "요청 URI 형식이 올바르지 않습니다"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "필수값이 누락되었거나 필드 유효성 검증에 실패했습니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리소스입니다"),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다"),
    TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 시간입니다"),
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 테마입니다"),
    DUPLICATED_RESERVATION(HttpStatus.CONFLICT, "같은 날짜, 시간, 테마 의 예약이 이미 존재합니다"),
    TIME_HAS_RESERVATIONS(HttpStatus.BAD_REQUEST, "예약이 존재하는 시간은 삭제할 수 없습니다"),
    THEME_HAS_RESERVATIONS(HttpStatus.BAD_REQUEST, "예약이 존재하는 테마는 삭제할 수 없습니다"),
    PAST_RESERVATION_CANCEL(HttpStatus.UNPROCESSABLE_ENTITY, "이미 지난 날짜의 예약은 취소할 수 없습니다"),
    PAST_RESERVATION_UPDATE(HttpStatus.UNPROCESSABLE_ENTITY, "이미 지난 날짜의 예약은 수정할 수 없습니다"),
    PAST_DATE_RESERVATION(HttpStatus.UNPROCESSABLE_ENTITY, "과거 날짜로 예약을 추가/변경할 수 없습니다"),
    INVALID_TIME_FORMAT(HttpStatus.UNPROCESSABLE_ENTITY, "예약은 1시간 단위로 가능합니다"),
    RESERVATION_CONCURRENT_MODIFICATION(HttpStatus.CONFLICT, "다른 사용자가 동시에 요청하여 예약 수정에 실패했습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 서버 오류가 발생했습니다"),
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
