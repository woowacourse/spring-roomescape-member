package roomescape.exception;

public class CustomException  extends RuntimeException {

    private final CustomExceptionCode exceptionCode;

    public CustomException(CustomExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public CustomExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
