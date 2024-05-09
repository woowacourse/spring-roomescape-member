package roomescape.reservation.handler.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private final CustomExceptionCode customExceptionCode;

    public CustomException(CustomExceptionCode customExceptionCode) {
        super(customExceptionCode.getErrorMessage());
        this.customExceptionCode = customExceptionCode;
    }

    public HttpStatus getHttpStatus() {
        return customExceptionCode.getHttpStatus();
    }
}
