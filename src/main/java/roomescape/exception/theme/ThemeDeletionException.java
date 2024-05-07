package roomescape.exception.theme;

import org.springframework.http.HttpStatus;

import roomescape.exception.CustomException;

public class ThemeDeletionException extends CustomException {
    public ThemeDeletionException() {
        super("테마 삭제 중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST);
    }
}
