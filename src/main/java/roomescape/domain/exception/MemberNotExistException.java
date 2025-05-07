package roomescape.domain.exception;

public class MemberNotExistException extends RuntimeException {

    private static final String message = "[ERROR] 이메일 혹은 비밀번호가 올바르지 않습니다.";

    public MemberNotExistException() {
        super(message);
    }
}
