package roomescape.exception;

//TODO 이름 변경 roomescapeException
public class CustomException extends RuntimeException {

    public CustomException(final String message) {
        super(message);
    }
}
