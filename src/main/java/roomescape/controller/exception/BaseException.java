package roomescape.controller.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {

    private final String title;
    private final String detail;
    private final HttpStatus status;

    public BaseException(String title, String detail, HttpStatus status) {
        super(title + " - " + detail);
        this.title = title;
        this.detail = detail;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
