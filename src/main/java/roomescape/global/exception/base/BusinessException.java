package roomescape.global.exception.base;

import org.springframework.http.HttpStatus;

public abstract class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public BusinessException(ErrorPolicy errorPolicy) {
        this.status = errorPolicy.status();
        this.message = errorPolicy.message();
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
