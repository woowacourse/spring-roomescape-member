package roomescape.business.service;

import org.springframework.stereotype.Service;
import roomescape.business.domain.Theme;
import roomescape.persistence.dao.ThemeDao;
import roomescape.presentation.dto.ThemeRequest;
import roomescape.presentation.dto.ThemeResponse;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(final ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeResponse create(final ThemeRequest themeRequest) {
        final Theme theme = themeRequest.toDomain();
        final Long id = themeDao.save(theme);

        return ThemeResponse.withId(id, theme);
    }
}
