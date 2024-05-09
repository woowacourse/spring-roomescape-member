package roomescape.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("해당 이메일의 회원이 이미 존재합니다.");
    }
}
