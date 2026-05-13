package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ProblemDetail handleException(Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "서버 내부 오류가 발생했습니다."
        );
        problemDetail.setTitle("Internal Server Error");
        return problemDetail;
    }

    @ExceptionHandler
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "입력값 검증에 실패했습니다."
        );
        problemDetail.setTitle("Validation Failed");
        return problemDetail;
    }

    @ExceptionHandler
    public ProblemDetail handleRoomescapeException(RoomescapeException e) {
        ErrorCode errorCode = e.getErrorCode();
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                errorCode.getStatus(),
                errorCode.getMessage()
        );
        problemDetail.setTitle(errorCode.name());
        return problemDetail;
    }
}
