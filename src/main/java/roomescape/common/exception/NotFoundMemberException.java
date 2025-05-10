package roomescape.common.exception;

public class NotFoundMemberException extends NotFoundException {
    public NotFoundMemberException() {
    }

    public NotFoundMemberException(String message) {
        super(message);
    }
}
