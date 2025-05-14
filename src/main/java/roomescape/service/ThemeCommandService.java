package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.request.ThemePostRequest;
import roomescape.dto.response.ThemeFullResponse;
import roomescape.entity.Theme;

@Service
public class ThemeCommandService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeCommandService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public ThemeFullResponse createTheme(ThemePostRequest themePostRequest) {
        Theme themeWithoutId = themePostRequest.toTheme();

        if (themeDao.existsByName(themeWithoutId)) {
            throw new IllegalArgumentException("이미 존재하는 테마명입니다.");
        }
        Theme theme = themeDao.create(themeWithoutId);
        return new ThemeFullResponse(theme);
    }

    public void deleteTheme(Long id) {
        boolean hasReservation = reservationDao.existByThemeId(id);
        if (hasReservation) {
            throw new IllegalArgumentException("해당 테마에 예약한 예약 정보가 있습니다.");
        }
        themeDao.deleteById(id);
    }
}
