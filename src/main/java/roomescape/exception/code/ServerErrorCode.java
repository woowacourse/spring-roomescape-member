package roomescape.exception.code;

import org.springframework.http.HttpStatus;

public enum ServerErrorCode implements ErrorCode {
    INTERNAL_SERVER_ERROR("요청 처리에 문제가 발생했습니다.");

    private final String message;

    ServerErrorCode(String message) {
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
