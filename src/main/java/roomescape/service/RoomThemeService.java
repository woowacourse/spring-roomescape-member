package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.RoomThemeDao;
import roomescape.domain.roomtheme.PopularThemeSelectionCriteria;
import roomescape.domain.roomtheme.RoomTheme;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.InUseException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.service.dto.RoomThemeCreation;

@Service
public class RoomThemeService {

    private static final int POPULAR_THEME_SELECTION_DURATION = 7;
    private static final int POPULAR_THEMES_TOP_COUNT = 10;

    private final ReservationDao reservationDAO;
    private final RoomThemeDao themeDAO;

    public RoomThemeService(final ReservationDao reservationDAO, final RoomThemeDao themeDAO) {
        this.reservationDAO = reservationDAO;
        this.themeDAO = themeDAO;
    }

    public RoomTheme addTheme(final RoomThemeCreation themeCreation) {
        validateThemeNotDuplicated(themeCreation);
        final RoomTheme theme =
                new RoomTheme(themeCreation.name(), themeCreation.description(), themeCreation.thumbnail());

        final long id = themeDAO.insert(theme);
        return findById(id);
    }

    private void validateThemeNotDuplicated(final RoomThemeCreation themeCreation) {
        if (themeDAO.existsByName(themeCreation.name())) {
            throw new ExistedDuplicateValueException("이미 존재하는 테마입니다");
        }
    }

    private RoomTheme findById(final long id) {
        return themeDAO.findById(id)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 테마입니다"));
    }

    public List<RoomTheme> findAllThemes() {
        return themeDAO.findAll();
    }

    public List<RoomTheme> findPopularThemes() {
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
