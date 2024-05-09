package roomescape.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ExceptionResponse exceptionResponse;

    public BusinessException(ErrorType errorType) {
        super(errorType.getMessage());
        this.httpStatus = errorType.getHttpStatus();
        this.exceptionResponse = new ExceptionResponse(errorType.getMessage());
    }

}
