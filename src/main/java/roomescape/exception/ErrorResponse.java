package roomescape.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ErrorResponse(
        String message,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime timestamp
) {
    public ErrorResponse(String message) {
        this(message, LocalDateTime.now());
    }
}
