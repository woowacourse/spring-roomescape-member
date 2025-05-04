package roomescape.exception;

public class DuplicatedEmailException extends RootException {

    private static final String MESSAGE = "중복된 이메일입니다.";

    public DuplicatedEmailException() {
        super(MESSAGE);
    }
}
