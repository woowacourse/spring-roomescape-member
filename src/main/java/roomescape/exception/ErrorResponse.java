package roomescape.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final String path;
    private final String message;
    private final LocalDateTime timeStamp;

    private ErrorResponse(String path, String message, LocalDateTime timeStamp) {
        this.path = path;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public static ErrorResponse of(String path, String message) {
        return new ErrorResponse(path, message, LocalDateTime.now());
    }
}
