package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.InUseException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.reservation.dao.ReservationDao;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.PopularThemeSelectionCriteria;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.dto.CreateThemeServiceRequest;

@Service
public class ThemeService {

    private static final int POPULAR_THEME_SELECTION_DURATION = 7;
    private static final int POPULAR_THEMES_TOP_COUNT = 10;

    private final ReservationDao reservationDAO;
    private final ThemeDao themeDAO;

    public ThemeService(final ReservationDao reservationDAO, final ThemeDao themeDAO) {
        this.reservationDAO = reservationDAO;
        this.themeDAO = themeDAO;
    }

    public Theme addTheme(final CreateThemeServiceRequest themeCreation) {
        validateThemeNotDuplicated(themeCreation);
        final Theme theme =
                new Theme(themeCreation.name(), themeCreation.description(), themeCreation.thumbnail());

        final long savedId = themeDAO.insert(theme);
        return new Theme(savedId, theme);
    }

    private void validateThemeNotDuplicated(final CreateThemeServiceRequest themeCreation) {
        if (themeDAO.existsByName(themeCreation.name())) {
            throw new ExistedDuplicateValueException("이미 존재하는 테마입니다");
        }
    }

    public List<Theme> findAllThemes() {
        return themeDAO.findAll();
    }

    public List<Theme> findPopularThemes() {
        PopularThemeSelectionCriteria criteria =
                new PopularThemeSelectionCriteria(LocalDate.now(), POPULAR_THEME_SELECTION_DURATION);

        return themeDAO.findPopularThemes(criteria.getStartDay(), criteria.getEndDay(), POPULAR_THEMES_TOP_COUNT);
    }

    public void deleteTheme(final long id) {
        validateThemeNotInUse(id);

        final boolean deleted = themeDAO.deleteById(id);

        if (!deleted) {
            throw new NotExistedValueException("존재하지 않는 테마입니다");
        }
    }

    private void validateThemeNotInUse(final long id) {
        if (reservationDAO.existsByThemeId(id)) {
            throw new InUseException("사용 중인 테마입니다");
        }
    }
}
