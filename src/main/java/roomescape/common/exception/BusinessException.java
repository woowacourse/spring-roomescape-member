package roomescape.common.exception;

import org.springframework.http.HttpStatus;

import java.util.Optional;

public abstract class BusinessException extends RuntimeException {

    private final String userMessage;
    private final boolean userVisible;

    protected BusinessException(final String message,
                                final String userMessage) {
        super(message);
        this.userMessage = userMessage;
        this.userVisible = true;
    }

    protected BusinessException(final String message) {
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
