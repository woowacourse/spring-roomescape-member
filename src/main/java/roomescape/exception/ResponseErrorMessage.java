package roomescape.exception;

public class ResponseErrorMessage {

    private String code;
    private String message;

    public ResponseErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseErrorMessage of(ErrorCode errorCode) {
        return new ResponseErrorMessage(errorCode.name(), errorCode.getMessage());
    }

    public static ResponseErrorMessage of(ErrorCode errorCode, String customMessage) {
        return new ResponseErrorMessage(errorCode.name(), customMessage);
    }

    public String getMessage() {
        return message;
    }
}
