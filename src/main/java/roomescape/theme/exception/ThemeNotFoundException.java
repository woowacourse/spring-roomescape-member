package roomescape.theme.exception;

import roomescape.common.exception.NotFoundException;

public class ThemeNotFoundException extends NotFoundException {

    public ThemeNotFoundException(Long id) {
        super("존재하지 않는 테마입니다. id=" + id);
    }
}
