package roomescape.exception.code;

import org.springframework.http.HttpStatus;

public enum ServerErrorCode implements ErrorCode {
    INTERNAL_SERVER_ERROR("요청 처리에 문제가 발생했습니다.", null);

    private final String message;
    private final String action;

    ServerErrorCode(String message, String action) {
        this.message = message;
        this.action = action;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getAction() {
        return action;
    }
}
