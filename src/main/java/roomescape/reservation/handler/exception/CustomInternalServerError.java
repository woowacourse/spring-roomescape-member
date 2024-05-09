package roomescape.reservation.handler.exception;

import org.springframework.http.HttpStatus;

public enum CustomInternalServerError implements CustomExceptionCode {
    FAIL_TO_REMOVE("삭제하지 못했습니다."),
    FAIl_TO_CREATE("생성하지 못했습니다.")
    ;

    private static final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    private final String errorMessage;

    CustomInternalServerError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
