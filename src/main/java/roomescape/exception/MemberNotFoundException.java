package roomescape.exception;

public class MemberNotFoundException extends ResourceNotFoundException {

    public MemberNotFoundException(final String message) {
        super(message);
    }
}
