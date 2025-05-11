package roomescape.global.exception.conflict;

public class MemberEmailConflictException extends ConflictException {
    public MemberEmailConflictException() {
        super("이메일");
    }
}
