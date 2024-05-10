package roomescape.handler.exception;

import org.springframework.http.HttpStatus;

public interface CustomExceptionCode {

    String getErrorMessage();

    HttpStatus getHttpStatus();
}
