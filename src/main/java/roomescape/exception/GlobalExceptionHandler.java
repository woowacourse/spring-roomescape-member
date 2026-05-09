package roomescape.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import roomescape.exception.exception.BadRequestException;
import roomescape.exception.exception.DuplicateException;
import roomescape.exception.exception.ForeignKeyConstraintException;
import roomescape.exception.exception.InvalidException;
import roomescape.exception.exception.NotFoundException;
import roomescape.exception.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(
            BadRequestException e
    ) {
        log.warn("BadRequestException 발생: {}", e.getMessage(), e);
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateException(
            DuplicateException e
    ) {
        log.warn("DuplicateException 발생: {}", e.getMessage(), e);
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(
            NotFoundException e
    ) {
        log.warn("NotFoundException 발생: {}", e.getMessage(), e);
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(ForeignKeyConstraintException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConstraintException(
            ForeignKeyConstraintException e
    ) {
        log.warn("ForeignKeyConstraintException 발생: {}", e.getMessage(), e);
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(InvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(
            InvalidException e
    ) {
        return ErrorResponse.of(
                GlobalErrorCode.BAD_REQUEST.getMessage(),
                e.getErrors()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(
            MethodArgumentTypeMismatchException e
    ) {
        log.warn("MethodArgumentTypeMismatchException 발생", e);

        return ErrorResponse.of(
                GlobalErrorCode.BAD_REQUEST.getMessage()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataIntegrityViolation(
            DataIntegrityViolationException e
    ) {
        log.warn("DataIntegrityViolationException 발생", e);

        return ErrorResponse.of(
                GlobalErrorCode.BAD_REQUEST.getMessage()
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoHandlerFound(
            NoResourceFoundException e
    ) {
        log.warn("NoResourceFoundException 발생", e);

        return ErrorResponse.of(
                GlobalErrorCode.NOT_FOUND.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(
            Exception e
    ) {
        log.error("Unexpected Exception 발생", e);

        return ErrorResponse.of(
                GlobalErrorCode.INTERNAL_SERVER_ERROR.getMessage()
        );
    }

}
