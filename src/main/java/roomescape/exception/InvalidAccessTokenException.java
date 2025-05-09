package roomescape.exception;

public class InvalidAccessTokenException extends RuntimeException {

    public InvalidAccessTokenException() {
        super("권한 인증이 불가능합니다.");
    }
}
