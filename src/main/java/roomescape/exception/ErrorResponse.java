package roomescape.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        HttpStatus status,
        String exceptionType,
        String message
) {
    public static ErrorResponse badRequest(Exception e) {
        return new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName(),
                e.getMessage()
        );
    }

    public static ErrorResponse internalServerError() {
        return new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "",
                "서버에서 심각한 오류가 발생하였습니다."
        );
    }
}
