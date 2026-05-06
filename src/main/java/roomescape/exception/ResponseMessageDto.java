package roomescape.exception;

import java.util.List;

public record ResponseMessageDto (
        List<String> message
) {
    public ResponseMessageDto(String message) {
        this(List.of(message));
    }
}
