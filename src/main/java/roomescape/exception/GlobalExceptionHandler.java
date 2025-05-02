package roomescape.exception;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.domain.exception.ImpossibleReservationException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ProblemDetail> handleInvalidInput(InvalidInputException e) {
        ErrorCode code = e.getErrorCode();
        ProblemDetail detail = ProblemDetail.forStatus(code.getStatus());
        detail.setTitle("입력값 오류");
        detail.setDetail(code.getMessage());
        detail.setProperty("code", code.name());
        return ResponseEntity.status(code.getStatus()).body(detail);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleCustomException(BusinessException e) {
        ErrorCode code = e.getErrorCode();
        ProblemDetail detail = ProblemDetail.forStatus(code.getStatus());
        detail.setTitle("비즈니스 규칙 위반");
        detail.setDetail(code.getMessage());
        detail.setProperty("code", code.name());
        return ResponseEntity.status(code.getStatus()).body(detail);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(NotFoundException e) {
        ErrorCode code = e.getErrorCode();
        ProblemDetail detail = ProblemDetail.forStatus(code.getStatus());
        detail.setTitle("리소스를 찾을 수 없습니다");
        detail.setDetail(e.getMessage());
        detail.setProperty("code", code.name());
        return ResponseEntity.status(code.getStatus()).body(detail);
    }

    @ExceptionHandler(ImpossibleReservationException.class)
    public ResponseEntity<ProblemDetail> handleImpossibleReservation(ImpossibleReservationException e) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("예약 불가");
        detail.setDetail(e.getMessage());
        detail.setProperty("code", "IMPOSSIBLE_RESERVATION");
        return ResponseEntity.badRequest().body(detail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("입력값 유효성 실패");
        problem.setDetail(message);
        problem.setProperty("code", "INVALID_INPUT");

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleMessageNotReadable(HttpMessageNotReadableException e) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("요청 본문 읽기 실패");
        detail.setDetail("요청 본문(JSON)이 잘못되어 처리할 수 없습니다.");
        detail.setProperty("code", "JSON_NOT_READABLE");

        return ResponseEntity.badRequest().body(detail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(Exception e) {
        log.error("Unhandled exception", e);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("서버 내부 오류");
        problem.setDetail("서버에 문제가 발생했습니다.");
        problem.setProperty("code", "INTERNAL_ERROR");

        return ResponseEntity.internalServerError().body(problem);
    }
}
