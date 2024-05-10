package roomescape.exception;

public class NullRequestParameterException extends RuntimeException{

    public NullRequestParameterException() {}

    public NullRequestParameterException(String message) {
        super(message);
    }
}
