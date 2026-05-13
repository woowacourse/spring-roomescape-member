package roomescape.exception.dto;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCode {
    // reservation
    DUPLICATED_RESERVATION(CONFLICT, "DUPLICATED_RESERVATION", "해당 날짜, 시간, 테마의 예약이 존재하여 예약할 수 없습니다."),
    INVALID_RESERVATION_DATE(CONFLICT, "INVALID_RESERVATION_DATE", "지난 날짜에는 예약할 수 없습니다."),
    INVALID_RESERVATION_TIME(CONFLICT, "INVALID_RESERVATION_TIME", "지난 시간에는 예약할 수 없습니다."),
    NOT_FOUND_RESERVATION(NOT_FOUND, "NOT_FOUND_RESERVATION", "예약이 존재하지 않습니다."),
    UNAUTHORIZED_RESERVATION_ACCESS(BAD_REQUEST, "UNAUTHORIZED_RESERVATION_ACCESS", "본인의 예약만 접근 가능합니다."),

    // theme
    NOT_FOUND_THEME(NOT_FOUND, "NOT_FOUND_THEME", "테마를 찾을 수 없습니다."),
    CANNOT_DELETE_THEME_IN_USE(CONFLICT, "CANNOT_DELETE_THEME_IN_USE", "해당 테마를 참조하는 예약 데이터가 존재하기 때문에 삭제할 수 없습니다."),
    DUPLICATED_THEME(CONFLICT, "DUPLICATED_THEME", "해당 테마가 이미 존재합니다."),

    // reservationTime
    NOT_FOUND_RESERVATION_TIME(NOT_FOUND, "NOT_FOUND_RESERVATION_TIME", "예약 시간을 찾을 수 없습니다."),
    CANNOT_DELETE_RESERVATION_TIME_IN_USE(CONFLICT, "CANNOT_DELETE_RESERVATION_TIME_IN_USE", "해당 시간을 참조하는 예약 데이터가 존재하기 때문에 삭제할 수 없습니다."),
    DUPLICATED_RESERVATION_TIME(CONFLICT, "DUPLICATED_RESERVATION_TIME", "해당 시간이 이미 존재합니다."),

    // common
    INTEGRITY_VIOLATION_ON_DELETE(CONFLICT, "INTEGRITY_VIOLATION_ON_DELETE", "데이터 무결성 위반으로 삭제에 실패했습니다."),
    INVALID_INPUT(BAD_REQUEST, "INVALID_INPUT", "입력값이 올바르지 않습니다."),
    INVALID_REQUEST_FORMAT(BAD_REQUEST, "INVALID_REQUEST_FORMAT", "입력값의 형식이 올바르지 않습니다."),
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "SERVER_ERROR", """
            서버 오류
            죄송합니다. 요청을 처리하는 중에 내부 서버 오류가 발생한 것으로 보입니다. 이 문제는 현재 엔지니어 팀에 전달되어 해결 중입니다.
            나중에 다시 시도해 주세요."""),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
