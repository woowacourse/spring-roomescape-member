package exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(Exception exception) {
        super("올바르지 않은 토큰입니다.", exception);
    }
}
