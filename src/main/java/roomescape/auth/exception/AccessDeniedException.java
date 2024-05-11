package roomescape.auth.exception;

public class AccessDeniedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "잘못된 접근입니다.";

    public AccessDeniedException() {
        super(DEFAULT_MESSAGE);
    }
}
