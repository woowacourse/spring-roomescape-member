package roomescape.exception;

public class ResponseErrorMessage {

    private String code;
    private String message;

    public ResponseErrorMessage(ErrorCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    public static ResponseErrorMessage of(ErrorCode code) {
        return new ResponseErrorMessage(code);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
