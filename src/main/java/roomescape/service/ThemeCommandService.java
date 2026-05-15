package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.response.ThemeResponse;
import roomescape.exception.ErrorMessage;
import roomescape.exception.custom.ConflictException;
import roomescape.repository.ThemeDao;

@Service
@RequiredArgsConstructor
public class ThemeCommandService {

    private final ThemeDao themeDao;

    public ThemeResponse create(String name, String thumbnailUrl, String description) {
        validateNoDuplicateTheme(name);
        Theme saved = themeDao.save(Theme.pending(name, thumbnailUrl, description));
        return ThemeResponse.from(saved);
    }

    public void delete(long id) {
        themeDao.delete(id);
    }

    private void validateNoDuplicateTheme(String name) {
        if (themeDao.existsByName(name)) {
            throw new ConflictException(ErrorMessage.DUPLICATE_THEME);
        }
    }
}
