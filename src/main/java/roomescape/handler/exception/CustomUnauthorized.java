package roomescape.handler.exception;

import org.springframework.http.HttpStatus;

public enum CustomUnauthorized implements CustomExceptionCode {
    NOT_AUTHORIZED("허가되지 않은 접근입니다."),
    NO_LOGIN_TOKEN("로그인 토큰이 존재하지 않습니다."),
    NOT_LOGIN("로그인하지 않은 상태입니다."),
    INCORRECT_PASSWORD("비밀번호가 일치하지 않습니다");

    private static final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    private final String errorMessage;

    CustomUnauthorized(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
