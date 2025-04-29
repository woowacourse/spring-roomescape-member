package roomescape.exception.custom;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException() {
        super();
    }

    public InvalidInputException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "잘못된 입력입니다.";
    }
}
