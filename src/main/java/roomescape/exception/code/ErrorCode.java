package roomescape.exception.code;

public enum ErrorCode {
    UNKNOWN_SERVER_ERROR("UNKNOWN_ERROR", "알 수 없는 오류가 발생했습니다."),
    NOT_SUPPORTED_METHOD("NOT_SUPPORTED_METHOD", "지원하지 않는 HTTP 메서드입니다."),
    VALIDATION_ERROR("VALIDATION_ERROR", "입력값이 유효하지 않습니다."),
    INVALID_REQUEST("INVALID_REQUEST", "잘못된 요청입니다.");

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
