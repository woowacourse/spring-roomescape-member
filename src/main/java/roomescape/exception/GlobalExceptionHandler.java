package roomescape.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(CustomInvalidRequestException.class)
//    public ErrorResponse handleCustomNotFoundException(CustomInvalidRequestException exception) {
//        log.warn("[Custom Error]", exception);
//
//        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(),
//                exception.getErrorCode().getMessage());
//    }
//
//    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
//    @ExceptionHandler(CustomUnprocessableEntityException.class)
//    public ErrorResponse handleCustomUnprocessableEntityException(CustomUnprocessableEntityException exception) {
//        log.warn("[Custom Error]", exception);
//
//        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), HttpStatus.UNPROCESSABLE_ENTITY.name(),
//                exception.getErrorCode().getMessage());
//    }
//
//    @ResponseStatus(HttpStatus.CONFLICT)
//    @ExceptionHandler(CustomConflictException.class)
//    public ErrorResponse handleCustomConflictException(CustomConflictException exception) {
//        log.warn("[Custom Error]", exception);
//
//        return new ErrorResponse(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name(),
//                exception.getErrorCode().getMessage());
//    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorResponse handleRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        log.warn("[Invalid API Access]", exception);

        return new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.name(),
                ErrorCode.INVALID_METHOD_REQUEST.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public ErrorResponse handleNoResourceFoundException(
            NoResourceFoundException exception) {
        log.warn("[Invalid API Access]", exception);

        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(),
                ErrorCode.INVALID_URL_REQUEST.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomInvalidRequestException.class)
    public ErrorResponse handleRequestValidException(CustomInvalidRequestException exception) {
        log.warn("[Invalid Request Error]", exception);

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
                exception.getErrorCode().getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CustomInvalidDomainException.class)
    public ErrorResponse handleDomainValidException(CustomInvalidDomainException exception) {
        log.error("[Domain Valid Error]", exception);

        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleOtherException(Exception exception) {
        log.error("[Internal Server Error]", exception);

        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }
}
