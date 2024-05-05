package roomescape.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.global.dto.response.ApiResponse;
import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.ConflictException;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleDateTimeParseException(final HttpMessageNotReadableException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(ErrorType.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Object> handleConflictException(final ConflictException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(e.getErrorType());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Object> handleException(final Exception e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(ErrorType.INTERNAL_SERVER_ERROR);
    }
}
