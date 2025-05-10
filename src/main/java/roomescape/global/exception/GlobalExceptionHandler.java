package roomescape.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.global.exception.error.ConflictException;
import roomescape.global.exception.error.ForbiddenException;
import roomescape.global.exception.error.InvalidRequestException;
import roomescape.global.exception.error.NotFoundException;
import roomescape.global.exception.error.UnauthorizedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidRequestException.class)
    public String handleBadRequestException(InvalidRequestException exception) {
        LOGGER.error(exception.getMessage());
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        LOGGER.error(exception.getMessage());
        return "요청 형식이 올바르지 않습니다.";
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorizedException(UnauthorizedException exception) {
        LOGGER.error(exception.getMessage());
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public String handleForbiddenException(ForbiddenException exception) {
        LOGGER.error(exception.getMessage());
        return exception.getMessage();
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException exception) {
        LOGGER.error(exception.getMessage());
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public String handleConflictException(ConflictException exception) {
        LOGGER.error(exception.getMessage());
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception) {
        LOGGER.error(exception.getMessage());
        exception.printStackTrace();
        return "서버 내부에서 오류가 발생했습니다.";
    }

}
