package roomescape.exception;

public class NullRequestParameterException extends BadRequestException{

    public NullRequestParameterException() {}

    public NullRequestParameterException(String message) {
        super(message);
    }
}
