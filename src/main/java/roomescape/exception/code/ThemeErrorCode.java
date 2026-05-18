package roomescape.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ThemeErrorCode implements ErrorCode {
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID를 갖는 테마는 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
