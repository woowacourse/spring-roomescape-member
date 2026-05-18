package roomescape.handler;

import jakarta.annotation.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Priority(3)
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("잘못된 요청 데이터 전달됨: ", e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "요청하신 데이터의 형식이 올바르지 않거나 필수 값이 누락되었습니다."
        );
        problemDetail.setTitle("잘못된 요청");

        return ResponseEntity
                .status(problemDetail.getStatus())
                .body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception e) {
        log.error("예상치 못한 예외 발생: ", e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
        );
        return ResponseEntity
                .status(problemDetail.getStatus())
                .body(problemDetail);
    }
}
