package roomescape.service;

import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.entity.Theme;

@Component
public class ThemeCommandService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeCommandService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
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
