package roomescape.domain.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime timestamp,
                            String message, List<ErrorDetail> errors
) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(LocalDateTime.now(), errorCode.getMessage(), null);
    }

    public static ErrorResponse of(ErrorCode errorCode, List<ErrorDetail> errors) {
        return new ErrorResponse(LocalDateTime.now(), errorCode.getMessage(), errors);
    }
}
