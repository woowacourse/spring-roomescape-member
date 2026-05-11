package roomescape.exception;

import static roomescape.exception.ErrorMessage.*;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;

public enum HttpErrorMapping {
    BAD_REQUEST_ERRORS(HttpStatus.BAD_REQUEST, List.of(
            INVALID_NAME_BLANK,
            INVALID_NAME_LENGTH,
            INVALID_DATE_NULL,
            INVALID_DATE_FORMAT,
            INVALID_TIME_ID_FORMAT,
            INVALID_THEME_ID_FORMAT,
            INVALID_START_TIME_NULL,
            INVALID_START_TIME_FORMAT,
            DUPLICATED_RESERVATION_REQUEST
    )),
    NOT_FOUNT_ERRORS(HttpStatus.NOT_FOUND, List.of(
            INVALID_THEME_ID,
            INVALID_RESERVATION_TIME_ID,
            INVALID_RESERVATION_ID
    )),
    CONFLICT_ERRORS(HttpStatus.CONFLICT, List.of(
            CANNOT_DELETE_RESERVATION_TIME_IN_USE,
            CANNOT_DELETE_THEME_IN_USE,
            INTEGRITY_VIOLATION_ON_DELETE
    )),
    ;

    private final HttpStatus httpStatus;
    private final List<ErrorMessage> errorMessages;

    HttpErrorMapping(HttpStatus httpStatus, List<ErrorMessage> errorMessages) {
        this.httpStatus = httpStatus;
        this.errorMessages = errorMessages;
    }

    public static HttpStatus getHttpStatus(ErrorMessage message) {
        return Arrays.stream(values())
                .filter(errorCode -> errorCode.errorMessages.contains(message))
                .findFirst()
                .map(mapper -> mapper.httpStatus)
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
