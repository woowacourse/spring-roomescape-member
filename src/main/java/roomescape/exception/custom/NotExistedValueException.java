package roomescape.exception.custom;

public class NotExistedValueException extends RuntimeException {

    public NotExistedValueException() {
        super();
    }

    public NotExistedValueException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 값 입니다";
    }
}
