package roomescape.common.exception;

import org.springframework.http.HttpStatus;

import java.util.Optional;

public abstract class BaseException extends RuntimeException {

    private final String userMessage;
    private final boolean userVisible;

    protected BaseException(final String logMessage,
                            final String userMessage) {
        super(logMessage);
        this.userMessage = userMessage;
        this.userVisible = true;
    }

    protected BaseException(final String message) {
        super(message);
        this.userMessage = "";
        this.userVisible = false;
    }

    public Optional<String> getUserMessage() {
        if (userVisible) {
            return Optional.of(userMessage);
        }
        return Optional.empty();
    }

    public abstract HttpStatus getHttpStatus();
}
