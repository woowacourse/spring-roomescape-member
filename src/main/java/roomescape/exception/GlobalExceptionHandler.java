package roomescape.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationExceptions(AuthorizationException ex) {
        log.warn("유효하지 않은 접근: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("예약을 취소할 권한이 없습니다."));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundExceptions(ResourceNotFoundException ex) {
        log.warn("요청한 리소스를 찾을 수 없음: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("예약이 존재하지 않습니다."));
    }

    @ExceptionHandler(SameScheduleException.class)
    public ResponseEntity<ErrorResponse> handleSameScheduleExceptions(SameScheduleException ex) {
        log.warn("동일한 스케줄로 변경 시도: {}", ex.getMessage());

        return ResponseEntity.badRequest().body(new ErrorResponse("기존과 동일한 스케줄로 변경할 수 없습니다."));
    }

    @ExceptionHandler(ReservationDeadlineException.class)
    public ResponseEntity<ErrorResponse> handleReservationDeadlineExceptions(ReservationDeadlineException ex) {
        log.warn("취소 및 변경 마감 기한 초과: {}", ex.getMessage());

        return ResponseEntity.badRequest().body(new ErrorResponse("방탈출 시작 1시간 전부터는 예약을 취소하거나 변경할 수 없습니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("예상치 못한 서버 내부 에러 발생!", ex);

        return ResponseEntity.internalServerError().body(new ErrorResponse("서버 내부에서 에러가 발생했습니다. 관리자에게 문의하세요."));
    }
}
