package roomescape.business.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.business.domain.Theme;
import roomescape.exception.DuplicateException;
import roomescape.exception.NotFoundException;
import roomescape.persistence.dao.ThemeDao;
import roomescape.presentation.dto.ThemeRequest;
import roomescape.presentation.dto.ThemeResponse;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(final ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeResponse insert(final ThemeRequest themeRequest) {
        validateNameIsNotDuplicate(themeRequest.name());
        final Theme theme = themeRequest.toDomain();
        final Long id = themeDao.insert(theme).getId();
        return ThemeResponse.withId(id, theme);
    }

    private void validateNameIsNotDuplicate(final String name) {
        if (themeDao.existsByName(name)) {
            throw new DuplicateException("추가 하려는 테마 이름이 이미 존재합니다.");
        }
    }

    public List<ThemeResponse> findAll() {
        return themeDao.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse findById(final Long id) {
        final Theme theme = themeDao.findById(id)
                .orElseThrow(() -> new NotFoundException("해당하는 테마를 찾을 수 없습니다. 테마 id: %d".formatted(id)));
        return ThemeResponse.from(theme);
    }

    public void deleteById(final Long id) {
        if (!themeDao.deleteById(id)) {
            throw new NotFoundException("해당하는 테마를 찾을 수 없습니다. 테마 id: %d".formatted(id));
        }
    }

    public List<ThemeResponse> findPopularThemes() {
        final LocalDate now = LocalDate.now();
        final String startDate = now.toString();
        final String endDate = now.minusDays(7).toString();

        return themeDao.findPopularThemesBetween(startDate, endDate).stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
