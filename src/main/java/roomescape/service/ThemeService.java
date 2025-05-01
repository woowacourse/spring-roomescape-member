package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.ThemeRequest;
import roomescape.controller.dto.ThemeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;
import roomescape.service.reservation.Theme;
import roomescape.service.reservation.ThemeName;

@Service
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(final ThemeDao themeDao, final ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public ThemeResponse createTheme(final ThemeRequest themeRequest) {
        final ThemeName name = new ThemeName(themeRequest.name());
        if (themeDao.isExists(name)) {
            throw new IllegalArgumentException("해당 이름의 테마는 이미 존재합니다.");
        }
        Theme savedTheme = themeDao.save(themeRequest.convertToTheme());
        return new ThemeResponse(savedTheme);
    }

    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeDao.findAll();
        return themes.stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public void deleteThemeById(final Long id) {
        if (reservationDao.isExistsByThemeId(id)) {
            throw new IllegalArgumentException("예약이 존재하는 테마는 삭제할 수 없습니다.");
        }
        themeDao.deleteById(id);
    }

    public List<ThemeResponse> findPopularThemes() {
        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now().minusDays(1);
        List<Theme> popularThemes = themeDao.findPopularThemes(from, to, 10);
        return popularThemes.stream()
                .map(ThemeResponse::new)
                .toList();
    }
}
