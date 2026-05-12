package roomescape.exception;

public class EmptyNameException extends IllegalArgumentException {

    private static final String ERROR_MESSAGE = "비어 있지 않은 이름을 입력해 주세요.";

    public EmptyNameException() {
        super(ERROR_MESSAGE);
    }
}
