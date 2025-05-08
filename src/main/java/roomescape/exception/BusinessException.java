package roomescape.exception;

public class BusinessException extends CustomException {
    public BusinessException(ErrorCode errorCode) {
        super(errorCode, "비즈니스 규칙 위반");
    }
}
