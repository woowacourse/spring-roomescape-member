package roomescape.common.dto;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.ErrorInformation;

public record ErrorDetail(
        int status,
        HttpStatus error,
        String errorCode,
        String message
) {

    public static ErrorDetail of(HttpStatus error, String errorCode, String message) {
        return new ErrorDetail(error.value(), error, errorCode, message);
    }

    public static ErrorDetail of(ErrorInformation errorInformation) {
        HttpStatus httpStatus = errorInformation.getHttpStatus();
        return new ErrorDetail(httpStatus.value(), httpStatus, errorInformation.getErrorCode(), errorInformation.getMessage());
    }

}
