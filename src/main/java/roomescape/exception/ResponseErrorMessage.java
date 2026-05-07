package roomescape.exception;

import java.util.List;

public record ResponseErrorMessage(
        List<String> message
) {
    public ResponseErrorMessage(String message) {
        this(List.of(message));
    }
}
