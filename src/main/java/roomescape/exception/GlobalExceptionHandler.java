package roomescape.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("[WARN] 읽을 수 없는 요청 (JSON 파싱 실패): {}", e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT_FORM", "요청 본문을 읽을 수 없습니다."));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("[WARN] @Valid 검증 실패: {}", errorMessage);
        return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT_VALUE", errorMessage));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("[WARN] 필수 파라미터 누락: {}", ex.getMessage());
        String message = String.format("필수 요청 파라미터('%s')가 누락되었습니다.", ex.getParameterName());
        return ResponseEntity.badRequest().body(new ErrorResponse("MISSING_PARAMETER", message));
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("[WARN] 데이터 타입 불일치: {}", ex.getMessage());
        String message = String.format("요청 값('%s')의 데이터 형식이 올바르지 않습니다.", ex.getValue());
        return ResponseEntity.badRequest().body(new ErrorResponse("TYPE_MISMATCH", message));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("[WARN] 지원하지 않는 HTTP 메서드 요청: {}", e.getMethod());
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResponse("METHOD_NOT_ALLOWED", "지원하지 않는 요청 방식입니다. GET/POST 등을 확인해주세요."));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("[WARN] 지원하지 않는 미디어 타입 (JSON이 아님): {}", e.getContentType());
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ErrorResponse("UNSUPPORTED_MEDIA_TYPE", "요청 데이터 형식이 잘못되었습니다. JSON 포맷인지 확인해주세요."));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception e, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("[WARN] Spring MVC 내부 예외 발생 : {}", e.getMessage());
        String errorCodeName = HttpStatus.valueOf(status.value()).name();
        String friendlyMessage = "요청 형식이 올바르지 않습니다. 시스템 관리자에게 문의해주세요.";

        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(errorCodeName, friendlyMessage));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        log.info("[INFO] 리소스 없음: {}", ex.getMessage());
        return createErrorResponse(ex.getErrorCode());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        log.warn("[WARN] 비즈니스 데이터 충돌: {}", ex.getMessage());
        // 슬랙 연동
        return createErrorResponse(ex.getErrorCode());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
        log.warn("[SECURITY] 권한 없는 접근 시도: {}", ex.getMessage());
        return createErrorResponse(ex.getErrorCode());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        log.warn("[WARN] 잘못된 비즈니스 요청: {}", ex.getMessage());
        return createErrorResponse(ex.getErrorCode());
    }

    @ExceptionHandler(CustomBusinessException.class)
    public ResponseEntity<ErrorResponse> handleCustomBusinessException(CustomBusinessException ex) {
        log.warn("[FALLBACK] 공통 비즈니스 에러 처리: {}", ex.getErrorCode().getMessage());
        return createErrorResponse(ex.getErrorCode());
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<ErrorResponse> handleBadSqlGrammarException(BadSqlGrammarException ex) {
        log.error("[CRITICAL] SQL 문법 오류 발생 (SQL: {}): {}", ex.getSql(), ex.getMessage(), ex);
        return createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CannotGetJdbcConnectionException.class)
    public ResponseEntity<ErrorResponse> handleCannotGetJdbcConnectionException(CannotGetJdbcConnectionException ex) {
        log.error("[CRITICAL] DB 커넥션 획득 실패 : {}", ex.getMessage(), ex);
        return createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleQueryTimeoutException(QueryTimeoutException ex) {
        log.error("[CRITICAL] DB 쿼리 실행 타임아웃 발생 : {}", ex.getMessage(), ex);
        return createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        log.error("[CRITICAL] 세분화되지 않은 예기치 못한 DB 에러 발생 : {}", ex.getMessage(), ex);
        return createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("[CRITICAL] 예상치 못한 서버 내부 에러 발생!", ex);
        // 슬랙 연동
        return createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.name(), errorCode.getMessage()));
    }
}
