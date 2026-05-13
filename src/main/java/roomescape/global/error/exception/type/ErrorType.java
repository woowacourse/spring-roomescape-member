package roomescape.global.error.exception.type;

import org.springframework.http.HttpStatus;

public interface ErrorType {

    HttpStatus status();

    String message();
}
