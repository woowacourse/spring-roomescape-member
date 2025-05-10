package roomescape.exception;

public class MemberExistException extends RuntimeException{

    public MemberExistException(ExceptionCause exceptionCause) {
        super(exceptionCause.getMessage());
    }
}
