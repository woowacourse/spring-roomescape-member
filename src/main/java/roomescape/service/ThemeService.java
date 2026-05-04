package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeResponse create(ThemeRequest request) {
        Theme theme = new Theme(
                null,
                request.name(),
                request.description(),
                request.url()
        );

        Theme saved = themeDao.save(theme);

        return ThemeResponse.from(saved);
    }
}
