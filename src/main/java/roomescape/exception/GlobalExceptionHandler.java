package roomescape.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final Map<Class<?>, ErrorCode> STANDARD_CODES = Map.of(
            MissingRequestHeaderException.class, ErrorCode.MISSING_HEADER,
            MethodArgumentTypeMismatchException.class, ErrorCode.INVALID_PARAMETER_TYPE,
            HttpMessageNotReadableException.class, ErrorCode.INVALID_REQUEST_BODY,
            MethodArgumentNotValidException.class, ErrorCode.VALIDATION_FAILED
    );

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                             HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (body instanceof ProblemDetail pd) {
            ErrorCode code = STANDARD_CODES.getOrDefault(ex.getClass(), ErrorCode.INTERNAL_ERROR);
            pd.setProperty("code", code.name());
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(org.springframework.http.HttpStatus.NOT_FOUND, e.getMessage());
        pd.setProperty("code", e.getCode().name());
        return pd;
    }

    @ExceptionHandler(EntityInUseException.class)
    public ProblemDetail handleEntityInUseException(EntityInUseException e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(org.springframework.http.HttpStatus.CONFLICT, e.getMessage());
        pd.setProperty("code", e.getCode().name());
        return pd;
    }

    @ExceptionHandler(OwnershipViolationException.class)
    public ProblemDetail handleOwnershipViolationException(OwnershipViolationException e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(org.springframework.http.HttpStatus.FORBIDDEN, e.getMessage());
        pd.setProperty("code", e.getCode().name());
        return pd;
    }

    @ExceptionHandler(DuplicateException.class)
    public ProblemDetail handleDuplicateException(DuplicateException e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(org.springframework.http.HttpStatus.CONFLICT, e.getMessage());
        pd.setProperty("code", e.getCode().name());
        return pd;
    }

    @ExceptionHandler(InvalidInputException.class)
    public ProblemDetail handleInvalidInputException(InvalidInputException e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(org.springframework.http.HttpStatus.BAD_REQUEST, e.getMessage());
        pd.setProperty("code", e.getCode().name());
        return pd;
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRuleException(BusinessRuleException e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        pd.setProperty("code", e.getCode().name());
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnhandledException(Exception e) {
        log.error("서버 내부 오류 발생", e);
        return ProblemDetail.forStatusAndDetail(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                "서버 내부 오류가 발생했습니다. 관리자에게 문의해주세요.");
    }
}
