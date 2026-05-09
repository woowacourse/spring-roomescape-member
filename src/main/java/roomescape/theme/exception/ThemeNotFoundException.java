package roomescape.theme.exception;

import roomescape.error.ErrorCode;
import roomescape.error.NotFoundException;

public class ThemeNotFoundException extends NotFoundException {
    private final Long id;

    public ThemeNotFoundException(Long id) {
        super(ErrorCode.THEME_NOT_FOUND, "테마가 존재하지 않습니다. id=" + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
