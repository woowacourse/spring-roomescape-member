package roomescape.auth.exception;

public class ExpiredTokenException extends RuntimeException {

    public ExpiredTokenException(Exception exception) {
        super("만료된 토큰입니다.", exception);
    }
}
