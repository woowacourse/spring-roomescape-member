package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ThemeNotFoundException extends RoomescapeException {
    public ThemeNotFoundException(long id) {
        super("해당 식별자의 테마를 찾을 수 없습니다. id: " + id, HttpStatus.NOT_FOUND);
    }
}
