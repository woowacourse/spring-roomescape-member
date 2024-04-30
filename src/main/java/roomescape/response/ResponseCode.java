package roomescape.response;

import org.springframework.http.HttpStatus;

public enum ResponseCode {

    SUCCESS_DELETE(HttpStatus.NO_CONTENT),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    FAILED_DELETE(HttpStatus.CONFLICT);

    private final HttpStatus httpStatus;

    ResponseCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
