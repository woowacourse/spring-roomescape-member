package roomescape.exception;

public class MemberNotFoundException extends RuntimeException{

    public MemberNotFoundException(ExceptionCause exceptionCause) {
        super(exceptionCause.getMessage());
    }
}
