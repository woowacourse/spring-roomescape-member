package roomescape.exception;

public class DataReferencedException extends BaseCustomException {
    public DataReferencedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
