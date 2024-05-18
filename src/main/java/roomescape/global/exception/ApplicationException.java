package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    private final ExceptionType exceptionType;

    public ApplicationException(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public HttpStatus getStatus() {
        return exceptionType.getStatus();
    }

    public String getMessage() {
        return exceptionType.getMessage();
    }
}
