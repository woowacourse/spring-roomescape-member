package roomescape.common.exception;

public abstract class BusinessException extends BaseException {

    protected BusinessException(final String message,
                                final String userMessage) {
        super(message, userMessage);
    }

    protected BusinessException(final String message) {
        super(message);
    }
}
