package roomescape.common.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCode {

    INVALID_REQUEST("Validation 오류입니다.", BAD_REQUEST),

    INVALID_RESERVATION_ID("예약 id는 비어 있을 수 없습니다.", BAD_REQUEST),
    INVALID_RESERVATION_GUEST_NAME("예약자 이름은 비어 있을 수 없습니다.", BAD_REQUEST),
    INVALID_RESERVATION_DATE("예약 날짜는 비어 있을 수 없습니다.", BAD_REQUEST),
    RESERVATION_CREATE_FAILED("예약 생성에 실패했습니다.", INTERNAL_SERVER_ERROR),
    RESERVATION_ALREADY_HAS_ID("이미 식별자가 존재하는 예약입니다.", CONFLICT),
    RESERVATION_ALREADY_EXISTS("이미 존재하는 예약입니다.", CONFLICT),
    RESERVATION_NOT_FOUND("존재하지 않는 예약입니다.", NOT_FOUND),
    PAST_RESERVATION_NOT_ALLOWED("이미 지난 시간에는 예약할 수 없습니다.", UNPROCESSABLE_ENTITY),
    CANNOT_EDIT_ALREADY_STARTED_RESERVATION("이미 시작된 예약은 수정할 수 없습니다.", UNPROCESSABLE_ENTITY),

    INVALID_RESERVATION_TIME_ID("예약 시간 id는 비어 있을 수 없습니다.", BAD_REQUEST),
    INVALID_RESERVATION_TIME("예약 시간은 비어 있을 수 없습니다.", BAD_REQUEST),
    RESERVATION_TIME_CREATE_FAILED("예약 시간 생성에 실패했습니다.", INTERNAL_SERVER_ERROR),
    RESERVATION_TIME_ALREADY_HAS_ID("이미 id가 존재하는 예약 시간입니다.", CONFLICT),
    RESERVATION_TIME_ALREADY_EXISTS("이미 존재하는 예약 시간입니다.", CONFLICT),
    RESERVATION_TIME_NOT_FOUND("존재하지 않는 예약 시간입니다.", NOT_FOUND),
    RESERVATION_TIME_HAS_RESERVATION("예약이 있는 시간은 삭제할 수 없습니다.", CONFLICT),

    INVALID_THEME_ID("테마 id는 비어 있을 수 없습니다.", BAD_REQUEST),
    INVALID_THEME_NAME("테마 이름은 비어 있을 수 없습니다.", BAD_REQUEST),
    INVALID_THEME_DESCRIPTION("테마 설명은 비어 있을 수 없습니다.", BAD_REQUEST),
    INVALID_THEME_THUMBNAIL("테마 썸네일은 비어 있을 수 없습니다.", BAD_REQUEST),
    INVALID_THEME("테마 정보는 비어 있을 수 없습니다.", BAD_REQUEST),
    THEME_ALREADY_HAS_ID("이미 id가 존재하는 테마입니다.", CONFLICT),
    THEME_NOT_FOUND("존재하지 않는 테마입니다.", NOT_FOUND),
    THEME_CREATE_FAILED("테마 생성에 실패했습니다.", INTERNAL_SERVER_ERROR),
    THEME_HAS_RESERVATION("예약이 있는 테마는 삭제할 수 없습니다.", CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.code = name();
        this.message = message;
        this.status = status;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    public HttpStatus status() {
        return status;
    }
}
