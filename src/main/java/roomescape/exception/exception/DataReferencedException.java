package roomescape.exception.exception;

public class DataReferencedException extends BaseCustomException {
    public DataReferencedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
