package roomescape.member.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.model.ExceptionCode;

public enum MemberExceptionCode implements ExceptionCode {

    ILLEGAL_EMAIL_FORM_EXCEPTION(HttpStatus.BAD_REQUEST, "이메일 형식이 맞지 않습니다."),
    ILLEGAL_PASSWORD_FORM_EXCEPTION(HttpStatus.BAD_REQUEST, "비밀번호는 숫자와 소문자로 이루어져야 합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    MemberExceptionCode(HttpStatus httpStatus, String message) {
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
