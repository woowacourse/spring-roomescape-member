package roomescape.exception;

public class MemberNotExistException extends NotAuthenticatedException {
    public MemberNotExistException(String message) {
        super(message);
    }
}
