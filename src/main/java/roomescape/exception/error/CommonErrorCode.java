package roomescape.exception.error;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {
    INVALID_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "요청하신 자원을 찾을 수 없어요"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에 오류가 발생했어요");


    private final HttpStatus status;
    private final String message;

    CommonErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
