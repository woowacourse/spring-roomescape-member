package roomescape.theme.domain.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.RoomescapeException;

public class ThemeNotFoundException extends RoomescapeException {

    public ThemeNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
