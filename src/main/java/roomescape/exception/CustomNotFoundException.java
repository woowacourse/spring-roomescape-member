package roomescape.exception;

public class CustomNotFoundException extends CustomException {

    public CustomNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
