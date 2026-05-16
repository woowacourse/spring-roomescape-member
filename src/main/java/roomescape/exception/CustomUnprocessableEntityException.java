package roomescape.exception;

public class CustomUnprocessableEntityException extends CustomException {

    public CustomUnprocessableEntityException(ErrorCode errorCode) {
        super(errorCode);
    }
}
