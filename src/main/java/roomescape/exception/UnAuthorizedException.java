package roomescape.exception;

public class UnAuthorizedException extends CustomException {
    private static final String message = "접근 권한이 없습니다.";

    public UnAuthorizedException() {
        super(message);
    }
}
