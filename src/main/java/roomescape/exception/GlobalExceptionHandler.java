package roomescape.exception;

import java.util.EnumMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<ErrorReason, HttpStatus> STATUS_MAP = new EnumMap<>(Map.ofEntries(
            Map.entry(ErrorReason.RESERVATION_NOT_FOUND, HttpStatus.NOT_FOUND),
            Map.entry(ErrorReason.RESERVATION_TIME_NOT_FOUND, HttpStatus.NOT_FOUND),
            Map.entry(ErrorReason.THEME_NOT_FOUND, HttpStatus.NOT_FOUND),
            Map.entry(ErrorReason.RESERVATION_DUPLICATE, HttpStatus.CONFLICT),
            Map.entry(ErrorReason.RESERVATION_TIME_IN_USE, HttpStatus.CONFLICT),
            Map.entry(ErrorReason.RESERVATION_ALREADY_CANCELED, HttpStatus.CONFLICT),
            Map.entry(ErrorReason.RESERVATION_NOT_OWNER, HttpStatus.FORBIDDEN),
            Map.entry(ErrorReason.PAST_RESERVATION, HttpStatus.UNPROCESSABLE_ENTITY)
    ));

    @ExceptionHandler
    public ProblemDetail handleRoomescapeException(RoomescapeException e) {
        HttpStatus status = STATUS_MAP.getOrDefault(e.getErrorReason(), HttpStatus.INTERNAL_SERVER_ERROR);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, e.getMessage());
        problemDetail.setTitle(e.getErrorReason().name());
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
    public ProblemDetail handleException(Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "서버 내부 오류가 발생했습니다."
        );
        problemDetail.setTitle("Internal Server Error");
        return problemDetail;
    }
}
