package roomescape.presentation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    private ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    private ErrorResponse handleUncaughtException(final Exception e) {
        e.printStackTrace();
        return new ErrorResponse("알 수 없는 에러가 발생했습니다.");
    }
}
