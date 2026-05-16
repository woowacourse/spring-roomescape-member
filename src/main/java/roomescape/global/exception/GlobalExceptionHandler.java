package roomescape.global.exception;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import roomescape.global.exception.exception.AlreadyExistsException;
import roomescape.global.exception.exception.AuthenticationFailedException;
import roomescape.global.exception.exception.InvalidUserInputException;
import roomescape.global.exception.exception.PermissionDeniedException;
import roomescape.global.exception.exception.ResourceInUseException;
import roomescape.global.exception.exception.ResourceNotFoundException;
import roomescape.global.exception.exception.UnexpectedUpdateCountException;
import roomescape.global.exception.exception.ValidationException;
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

    @ExceptionHandler(InvalidUserInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidUserInputException(
            InvalidUserInputException e
    ) {
        log.info("InvalidUserInputException 발생: {}", e.getMessage());
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExistsException(
            AlreadyExistsException e
    ) {
        log.info("AlreadyExistsException 발생: {}", e.getMessage());
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handlePermissionDeniedException(
            PermissionDeniedException e
    ) {
        log.info("PermissionDeniedException 발생: {}", e.getMessage());
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(
            ResourceNotFoundException e
    ) {
        log.info("ResourceNotFoundException 발생: {}", e.getMessage());
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(
            ValidationException e
    ) {
        log.info("ValidationException 발생: {}", e.getMessage());
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


    @ExceptionHandler(ResourceInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleResourceInUseException(
            ResourceInUseException e
    ) {
        log.warn("ResourceInUseException 발생: {}", e.getMessage(), e);
        return ErrorResponse.of(e.getMessage());
    }


    @ExceptionHandler(AuthenticationFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationFailedException(
            AuthenticationFailedException e
    ) {
        log.warn("AuthenticationFailedException 발생: {}", e.getMessage(), e);
        return ErrorResponse.of(
                GlobalErrorCode.AUTHENTICATION_FAILED.getMessage()
        );
    }

    @ExceptionHandler(UnexpectedUpdateCountException.class)
    public ErrorResponse handleUnexpectedUpdateCount(
            UnexpectedUpdateCountException e
    ) {
        log.error("UnexpectedUpdateCountException 발생: {}", e.getMessage(), e);

        return ErrorResponse.of(e.getMessage());
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
