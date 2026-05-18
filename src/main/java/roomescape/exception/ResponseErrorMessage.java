package roomescape.exception;

public record ResponseErrorMessage(String code, int status, String message) {

    public static ResponseErrorMessage of(ErrorCode errorCode) {
        return new ResponseErrorMessage(
                errorCode.name(),
                errorCode.getStatus().value(), 
                errorCode.getMessage()
        );
    }

    public static ResponseErrorMessage of(ErrorCode errorCode, String customMessage) {
        return new ResponseErrorMessage(
                errorCode.name(),
                errorCode.getStatus().value(),
                customMessage
        );
    }
}