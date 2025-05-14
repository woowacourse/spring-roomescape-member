package roomescape.global.exception;

public enum GlobalErrorCode implements ErrorCode {
    INVALID_INPUT_VALUE("G001", "입력값이 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR("G002", "서버 오류가 발생했습니다.");
    private final String code;
    private final String message;

    GlobalErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
