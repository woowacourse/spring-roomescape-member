package roomescape.exception;


//TODO 예외 사용하는 곳으로 패키지 이동
public class CustomException extends RuntimeException {

    public CustomException(final String message) {
        super(message);
    }
}
