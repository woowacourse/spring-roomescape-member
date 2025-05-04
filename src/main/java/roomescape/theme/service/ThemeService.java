package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.Dao;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.RankedThemeResponse;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;

@Service
public class ThemeService {
    private static final String THEME_ALREADY_EXISTS_EXCEPTION_MESSAGE = "[ERROR] 이미 테마가 존재합니다.";
    private static final String INVALID_THEME_ID_EXCEPTION_MESSAGE = "[ERROR] 해당 테마 아이디는 존재하지 않습니다";
    private static final String RESERVED_THEME_ID_EXCEPTION_MESSAGE = "[ERROR] 이미 예약된 테마는 삭제할 수 없습니다.";

    private final Dao<Reservation> reservationDao;
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao, Dao<Reservation> reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeResponse> findAll() {
        return themeDao.findAll().stream()
                .map(theme -> new ThemeResponse(
                        theme.getId(),
                        theme.getName(),
                        theme.getDescription(),
                        theme.getThumbnail()))
                .toList();
    }

    public List<RankedThemeResponse> findRankedByPeriod() {
        List<Theme> topRankedThemes = themeDao.findRankedByPeriod(
                LocalDate.now().minusDays(7),
                LocalDate.now().minusDays(1)
        );
        return topRankedThemes.stream()
                .map(theme -> new RankedThemeResponse(
                        theme.getName(),
                        theme.getDescription(),
                        theme.getThumbnail())
                )
                .toList();
    }

    public ThemeResponse add(final ThemeRequest themeRequest) {
        validateDuplicate(themeRequest);
        Theme newTheme = new Theme(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        Theme savedTheme = themeDao.add(newTheme);

        return new ThemeResponse(
                savedTheme.getId(),
                savedTheme.getName(),
                savedTheme.getDescription(),
                savedTheme.getThumbnail()
        );
    }

    private void validateDuplicate(ThemeRequest themeRequest) {
        List<Theme> themes = themeDao.findAll();

        boolean isDuplicate = themes.stream()
                .anyMatch(theme -> theme.getName().equals(themeRequest.name()));

        if (isDuplicate) {
            throw new IllegalArgumentException(THEME_ALREADY_EXISTS_EXCEPTION_MESSAGE);
        }
    }

    public void deleteById(Long id) {
        searchThemeId(id);
        List<Reservation> reservations = reservationDao.findAll();

        validateUnoccupiedThemeId(id, reservations);
        themeDao.deleteById(id);
    }

    private void searchThemeId(Long id) {
        themeDao.findById(id).orElseThrow(() -> new IllegalArgumentException(INVALID_THEME_ID_EXCEPTION_MESSAGE));
    }

    private void validateUnoccupiedThemeId(Long id, List<Reservation> reservations) {
        boolean isOccupiedThemeId = reservations.stream()
                .anyMatch(reservation -> reservation.getThemeId().equals(id));

        if (isOccupiedThemeId) {
            throw new IllegalArgumentException(RESERVED_THEME_ID_EXCEPTION_MESSAGE);
        }
    }
}
