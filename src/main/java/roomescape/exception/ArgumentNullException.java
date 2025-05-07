package roomescape.exception;

public class ArgumentNullException extends CustomException {

    private static final String message = "값이 존재하지 않습니다.";

    public ArgumentNullException() {
        super(message);
    }
}
