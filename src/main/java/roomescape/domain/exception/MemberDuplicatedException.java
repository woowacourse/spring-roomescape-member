package roomescape.domain.exception;

public class MemberDuplicatedException extends IllegalArgumentException {

    private static final String message = "[ERROR] 해당 이메일에 대한 회원이 이미 존재합니다.";

    public MemberDuplicatedException() {
        super(message);
    }
}
