package roomescape.auth.sign.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.util.ExceptionMessageFormatter;

public class InvalidSignInException extends AuthException {

    public InvalidSignInException(final Object... params) {
        super(
                buildLogMessage(params),
                buildUserMessage()
        );
    }

    private static String buildLogMessage(final Object... params) {
        return ExceptionMessageFormatter.format("failed to SignIn", params);
    }

    private static String buildUserMessage() {
        return "로그인이 실패했습니다.";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
