package roomescape.exception;

import java.util.List;

public record ErrorMessageResponse(
    List<String> messages
) {

    public ErrorMessageResponse(String message) {
        this(List.of(message));
    }
}
