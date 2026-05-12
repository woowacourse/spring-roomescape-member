package roomescape.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final String path;
    private final String code;
    private final String message;
    private final List<String> messages;
    private final LocalDateTime timeStamp;

    private ErrorResponse(String path, String code, String message, List<String> messages, LocalDateTime timeStamp) {
        this.path = path;
        this.code = code;
        this.message = message;
        this.messages = messages;
        this.timeStamp = timeStamp;
    }

    public static ErrorResponse of(String path, ErrorCode errorCode) {
        return new ErrorResponse(path, errorCode.code(), errorCode.message(), null, LocalDateTime.now());
    }

    public static ErrorResponse of(String path, String message) {
        return new ErrorResponse(path, null, message, null, LocalDateTime.now());
    }

    public static ErrorResponse of(String path, ErrorCode errorCode, List<String> messages) {
        List<String> copiedMessages = List.copyOf(messages);
        return new ErrorResponse(path, errorCode.code(), errorCode.message(), copiedMessages, LocalDateTime.now());
    }
}
