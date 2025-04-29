package roomescape.exception.custom;

public class ExistedDuplicateValueException extends RuntimeException {

    public ExistedDuplicateValueException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "중복된 값이 존재합니다";
    }
}
