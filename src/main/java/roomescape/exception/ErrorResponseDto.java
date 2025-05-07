package roomescape.exception;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        LocalDateTime timestamp
) {

    public ErrorResponseDto(String message) {
        this(message, LocalDateTime.now());
    }
}
