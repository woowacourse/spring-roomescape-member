package roomescape.global;

import org.springframework.http.HttpStatus;

public interface ErrorPolicy {
    HttpStatus status();
    String message();
}
