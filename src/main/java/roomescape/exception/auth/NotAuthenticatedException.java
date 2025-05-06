package roomescape.exception.auth;

public class NotAuthenticatedException extends RuntimeException {

    private static final String MESSAGE = "인증되지 않은 사용자입니다.";

    public NotAuthenticatedException() {
        super(MESSAGE);
    }
}
