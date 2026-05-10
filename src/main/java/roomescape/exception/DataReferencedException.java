package roomescape.exception;

public class DataReferencedException extends BaseCustomException {
    public DataReferencedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
