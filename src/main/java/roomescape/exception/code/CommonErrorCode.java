package roomescape.exception.code;

import org.springframework.http.HttpStatus;
import roomescape.exception.ErrorCode;

public enum CommonErrorCode implements ErrorCode {

    INVALID_REQUEST_PARAMETER(
            "C-400-001",
            HttpStatus.BAD_REQUEST,
            "요청 파라미터 형식이 올바르지 않습니다."
    ),

    INVALID_REQUEST_BODY(
            "C-400-002",
            HttpStatus.BAD_REQUEST,
            "요청 값이 올바르지 않습니다."
    ),

    INTERNAL_SERVER_ERROR(
            "C-500-001",
            HttpStatus.INTERNAL_SERVER_ERROR,
            "요청 처리 중 서버에 문제가 발생했습니다."
    );

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    CommonErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
