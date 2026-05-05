package roomescape.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeResponse addTheme(ThemeRequest request) {
        Theme theme = request.toTheme();
        validateTheme(theme);
        Theme savedTheme = themeDao.insert(theme);
        return ThemeResponse.from(savedTheme);
    }

    private void validateTheme(Theme theme) {
        Optional<Theme> newTheme = themeDao.selectById(theme.getId());
        if (newTheme.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 테마입니다.");
        }
    }

    public void delete(long themeId) {
        themeDao.selectById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        themeDao.delete(themeId);
    }
}
