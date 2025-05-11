package roomescape.business.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.business.domain.Theme;
import roomescape.exception.ThemeNotFoundException;
import roomescape.persistence.dao.ThemeDao;
import roomescape.persistence.entity.ReservationEntity;
import roomescape.presentation.dto.theme.ThemeRequest;
import roomescape.presentation.dto.theme.ThemeResponse;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(final ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public Theme find(final Long id) {
        return themeDao.find(id)
                .orElseThrow(() -> new ThemeNotFoundException(id));
    }

    public ThemeResponse create(final ThemeRequest themeRequest) {
        final Theme theme = themeRequest.toDomain();
        final Long id = themeDao.save(theme);

        return ThemeResponse.withId(id, theme);
    }

    public List<ThemeResponse> findAll() {
        return themeDao.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void remove(final Long id) {
        if (!themeDao.remove(id)) {
            throw new ThemeNotFoundException(id);
        }
    }

    public List<ThemeResponse> findPopularThemes() {
        final LocalDate now = LocalDate.now();
        final String startDate = ReservationEntity.formatDate(now.minusDays(8));
        final String endDate = ReservationEntity.formatDate(now.minusDays(1));
        final int limit = 10;

        return themeDao.findPopularThemesBetweenWithLimit(startDate, endDate, limit).stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
