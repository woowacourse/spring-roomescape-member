package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.entity.Theme;

@Component
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeResponse> findAllThemes() {
        return themeDao.findAll().stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public List<ThemeResponse> findThemeRank() {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusDays(8);
        LocalDate endDate = now.minusDays(1);
        List<Long> themeIds = reservationDao.findMostReservedThemeIdsBetween(startDate, endDate);
        List<Theme> themes = themeDao.findAllById(themeIds);

        return themes.stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public ThemeResponse createTheme(ThemeRequest themeRequest) {
        Theme themeWithoutId = themeRequest.toTheme();
        Theme theme = themeDao.create(themeWithoutId);
        return new ThemeResponse(theme);
    }

    public void deleteTheme(Long id) {
        boolean hasReservation = reservationDao.existByThemeId(id);
        if (hasReservation) {
            throw new IllegalArgumentException("해당 테마에 예약한 예약 정보가 있습니다.");
        }
        themeDao.deleteById(id);
    }
}
