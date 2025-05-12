package roomescape.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.http.HttpStatus;

public enum ErrorMessage {

    ONLY_ACCESS_ADMIN("어드민만 접근할 수 있는 페이지 입니다.", FORBIDDEN),
    INVALID_AUTHORIZATION("잘못된 인증 정보입니다.", UNAUTHORIZED),
    INVALID_TOKEN("잘못된 토큰 정보 입니다.", UNAUTHORIZED),

    NOT_FOUND_ID("해당 ID를 찾을 수 없습니다.", BAD_REQUEST),
    NOT_FOUND_USER("해당 유저를 찾을 수 없습니다.", BAD_REQUEST),

    DUPLICATE_USER("이미 존재하는 유저입니다.", BAD_REQUEST),
    DUPLICATE_RESERVATION("중복된 테마 및 날짜와 시간을 예약할 수 없습니다.", BAD_REQUEST),

    ALREADY_USED_RESOURCE("해당 자원은 다른 곳에서 사용중입니다.", BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
