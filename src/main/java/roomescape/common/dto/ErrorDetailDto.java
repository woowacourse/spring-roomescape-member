package roomescape.common.dto;

import java.time.LocalDateTime;

public record ErrorDetailDto(
        int status,
        String message,
        LocalDateTime timestamp
) {
    public static ErrorDetailDto of(int status, String message) {
        return new ErrorDetailDto(status, message, LocalDateTime.now());
    }
}
