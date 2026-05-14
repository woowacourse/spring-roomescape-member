package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionInformation {
    HttpStatus getHttpStatus();
    String getMessage();
}
