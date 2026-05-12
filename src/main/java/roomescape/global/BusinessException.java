package roomescape.global;

public abstract class BusinessException extends RuntimeException {

    private final String message;
    private final String code;

    public BusinessException(ErrorCode errorCode) {
        this.message = errorCode.message();
        this.code = errorCode.code();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
