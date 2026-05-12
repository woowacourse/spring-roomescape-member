package roomescape.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String message;

    public static ErrorResponse from(String message) {
        return new ErrorResponse(message);
    }
}
