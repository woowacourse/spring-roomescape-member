package roomescape.common.exception.code;

import org.springframework.http.HttpStatus;
import roomescape.exception.code.ErrorCode;

public enum TestErrorCode implements ErrorCode {
    TEST_BUSINESS_ERROR(HttpStatus.BAD_REQUEST, "테스트용 비즈니스 예외코드 입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    TestErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
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
