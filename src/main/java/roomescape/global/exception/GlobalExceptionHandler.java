package roomescape.global.exception;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import roomescape.global.exception.exception.AuthenticationException;
import roomescape.global.exception.exception.BadRequestException;
import roomescape.global.exception.exception.DuplicateException;
import roomescape.global.exception.exception.ForbiddenException;
import roomescape.global.exception.exception.ForeignKeyConstraintException;
import roomescape.global.exception.exception.InvalidException;
import roomescape.global.exception.exception.NotFoundException;
import roomescape.global.exception.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoHandlerFound(
            NoResourceFoundException e
    ) {
        return ErrorResponse.of(
                GlobalErrorCode.NOT_FOUND.getMessage()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(
            BadRequestException e
    ) {
        log.info("BadRequestException 발생: {}", e.getMessage());
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateException(
            DuplicateException e
    ) {
        log.info("DuplicateException 발생: {}", e.getMessage());
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handlerForbiddenException(
            ForbiddenException e
    ) {
        log.info("ForbiddenException 발생: {}", e.getMessage());
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(
            NotFoundException e
    ) {
        log.info("NotFoundException 발생: {}", e.getMessage());
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(InvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(
            InvalidException e
    ) {
        log.info("InvalidException 발생: {}", e.getMessage());
        return ErrorResponse.of(
                GlobalErrorCode.BAD_REQUEST.getMessage(),
                e.getErrors()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(
            MethodArgumentNotValidException e
    ) {

        log.info("MethodArgumentNotValidException 발생: {}", e.getMessage());
        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ErrorResponse.of(
                GlobalErrorCode.BAD_REQUEST.getMessage(),
                errors
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTypeMismatch(
            MethodArgumentTypeMismatchException e
    ) {
        log.info("MethodArgumentTypeMismatchException 발생: {}", e.getMessage());

        return ErrorResponse.of(
                GlobalErrorCode.BAD_REQUEST.getMessage(),
                List.of(e.getName() + "은(는) 올바른 타입이 아닙니다.")
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotReadable(
            HttpMessageNotReadableException e
    ) {
        log.info("HttpMessageNotReadableException 발생: {}", e.getMessage());
        return ErrorResponse.of(GlobalErrorCode.INVALID_JSON.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingParameter(
            MissingServletRequestParameterException e
    ) {
        log.info("MissingServletRequestParameterException 발생: {}", e.getMessage());
        return ErrorResponse.of(
                GlobalErrorCode.BAD_REQUEST.getMessage(),
                List.of(e.getParameterName() + " 파라미터가 누락되었습니다.")
        );
    }


    @ExceptionHandler(ForeignKeyConstraintException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConstraintException(
            ForeignKeyConstraintException e
    ) {
        log.warn("ForeignKeyConstraintException 발생: {}", e.getMessage(), e);
        return ErrorResponse.of(e.getMessage());
    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(
            AuthenticationException e
    ) {
        log.warn("AuthenticationException 발생: {}", e.getMessage(), e);
        return ErrorResponse.of(
                GlobalErrorCode.AUTHENTICATION_FAILED.getMessage()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataIntegrityViolation(
            DataIntegrityViolationException e
    ) {
        log.error("DataIntegrityViolationException 발생", e);

        return ErrorResponse.of(GlobalErrorCode.BAD_REQUEST.getMessage());
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
