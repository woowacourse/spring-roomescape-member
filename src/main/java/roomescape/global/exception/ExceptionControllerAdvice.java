package roomescape.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.global.dto.response.ApiResponse;
import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.AssociatedDataExistsException;
import roomescape.global.exception.model.CustomException;
import roomescape.global.exception.model.DataDuplicateException;
import roomescape.global.exception.model.ForbiddenException;
import roomescape.global.exception.model.NotFoundException;
import roomescape.global.exception.model.UnauthorizedException;
import roomescape.global.exception.model.ValidateException;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    // TODO: errorType 대신, exceptionMessage 던지기(errorType 이름도 같이 던져줄까 고민되지만 중복내용이기에 필요성 고려)
    @ExceptionHandler(value = {NotFoundException.class, ValidateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleNotFoundException(final CustomException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(e.getErrorType());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(ErrorType.INVALID_REQUEST_DATA_TYPE);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Object> handleUnauthorizedException(final UnauthorizedException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(e.getErrorType());
    }

    @ExceptionHandler(value = ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<Object> handleForbiddenException(final ForbiddenException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(e.getErrorType());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResponse<Object> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(ErrorType.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = {
            DataDuplicateException.class, AssociatedDataExistsException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Object> handleConflictException(final CustomException e) {
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
