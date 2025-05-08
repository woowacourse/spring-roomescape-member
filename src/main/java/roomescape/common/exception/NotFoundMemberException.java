package roomescape.common.exception;

public class NotFoundMemberException extends NotFoundException {
    public NotFoundMemberException() {
        super("해당하는 회원이 없습니다.");
    }

    public NotFoundMemberException(String message) {
        super(message);
    }
}
