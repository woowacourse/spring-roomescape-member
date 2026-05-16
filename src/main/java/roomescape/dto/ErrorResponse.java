package roomescape.dto;

import java.util.List;

public record ErrorResponse(
        String code,
        String path,
        String message,
        List<FieldErrorResponse> errors
) {
    public ErrorResponse(String code, String path, String message) {
        this(code, path, message, List.of());
    }

}