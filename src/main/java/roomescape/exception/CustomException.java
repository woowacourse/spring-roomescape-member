package roomescape.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {

    private final HttpStatus httpStatus;

    protected CustomException(HttpStatus httpStatus, ExceptionCause exceptionCause) {
        super(exceptionCause.getMessage());
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
