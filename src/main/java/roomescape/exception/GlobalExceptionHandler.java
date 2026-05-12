package roomescape.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("입력값 유효성 검증 실패: {}", errorMessage);

        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBusinessExceptions(IllegalArgumentException ex) {
        log.warn("비즈니스 로직 에러: {}", ex.getMessage());

        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationExceptions(DataIntegrityViolationException ex) {
        log.warn("데이터베이스 제약 조건 위반 발생: {}", ex.getMessage());

        return ResponseEntity.badRequest().body(new ErrorResponse("중복된 데이터이거나 유효하지 않은 요청입니다."));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderExceptions(MissingRequestHeaderException ex) {
        log.warn("필수 요청 헤더 누락: {}", ex.getMessage());

        return ResponseEntity.badRequest().body(new ErrorResponse("필수 요청 헤더 '" + ex.getHeaderName() + "'가 누락되었습니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("예상치 못한 서버 내부 에러 발생!", ex);

        return ResponseEntity.internalServerError().body(new ErrorResponse("서버 내부에서 에러가 발생했습니다. 관리자에게 문의하세요."));
    }
}
