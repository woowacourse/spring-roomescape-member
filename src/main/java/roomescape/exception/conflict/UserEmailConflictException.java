package roomescape.exception.conflict;

public class UserEmailConflictException extends ConflictException {
    public UserEmailConflictException() {
        super("이메일");
    }
}
