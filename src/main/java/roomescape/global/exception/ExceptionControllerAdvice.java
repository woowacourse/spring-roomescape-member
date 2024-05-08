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
import roomescape.global.exception.model.DataDuplicateException;
import roomescape.global.exception.model.ValidateException;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = ValidateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleValidateException(final ValidateException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(e.getErrorType());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(ErrorType.INVALID_REQUEST_DATA_TYPE);
    }

    @ExceptionHandler(value = DataDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Object> handleConflictException(final DataDuplicateException e) {
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
