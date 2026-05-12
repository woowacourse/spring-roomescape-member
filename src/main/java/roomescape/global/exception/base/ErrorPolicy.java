package roomescape.global.exception.base;

import org.springframework.http.HttpStatus;

public interface ErrorPolicy {
    HttpStatus status();
    String message();
}
