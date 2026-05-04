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
        Optional<Theme> newTheme = themeDao.selectById(theme.getId());
        validateUniqueTheme(newTheme);

        Theme savedTheme = themeDao.insert(theme);
        return ThemeResponse.from(savedTheme);
    }

    public void delete(long themeId) {
        themeDao.selectById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        themeDao.delete(themeId);
    }

    private void validateUniqueTheme(Optional<Theme> theme) {
        if (theme.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 테마입니다.");
        }
    }
}
