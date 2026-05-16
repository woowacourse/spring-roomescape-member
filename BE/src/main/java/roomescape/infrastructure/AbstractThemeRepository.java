package roomescape.infrastructure;

import roomescape.entity.Theme;
import roomescape.entity.ThemeRepository;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.NotFoundException;

public abstract class AbstractThemeRepository implements ThemeRepository {

    @Override
    public Theme getById(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.THEME_NOT_FOUND));
    }
}
