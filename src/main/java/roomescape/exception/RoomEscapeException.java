package roomescape.exception;

import org.springframework.http.HttpStatus;

import roomescape.exception.message.ExceptionMessage;

public class RoomEscapeException extends RuntimeException {
    private final ExceptionMessage exceptionMessage;

    public RoomEscapeException(final ExceptionMessage exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public String getMessage() {
        return exceptionMessage.getMessage();
    }

    public HttpStatus getStatus() {
        return exceptionMessage.getStatus();
    }
}
