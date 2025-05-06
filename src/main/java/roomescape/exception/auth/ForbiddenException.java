package roomescape.exception.auth;

public class ForbiddenException extends RuntimeException {

    private static final String MESSAGE = "권한이 없는 요청입니다.";

    public ForbiddenException() {
        super(MESSAGE);
    }
}
