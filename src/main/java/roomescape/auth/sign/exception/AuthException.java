package roomescape.auth.sign.exception;

import roomescape.common.exception.BaseException;

public abstract class AuthException extends BaseException {

    protected AuthException(final String logMessage,
                            final String userMessage) {
        super(logMessage, userMessage);
    }
}
