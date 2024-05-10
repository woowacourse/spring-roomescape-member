package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dao.ReservationThemeDao;
import roomescape.reservation.domain.ReservationTheme;
import roomescape.reservation.dto.theme.ThemeRequest;
import roomescape.reservation.dto.theme.ThemeResponse;
import roomescape.reservation.dto.theme.WeeklyThemeResponse;

@Service
public class ReservationThemeService {

    private final ReservationDao reservationDao;
    private final ReservationThemeDao reservationThemeDao;

    public ReservationThemeService(ReservationDao reservationDao, ReservationThemeDao reservationThemeDao) {
        this.reservationDao = reservationDao;
        this.reservationThemeDao = reservationThemeDao;
    }

    public List<ThemeResponse> getAllThemes() {
        return reservationThemeDao.findAll().stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public ThemeResponse insertTheme(ThemeRequest request) {
        ReservationTheme theme = getTheme(request);
        ReservationTheme inserted = reservationThemeDao.insert(theme);

        return new ThemeResponse(inserted);
    }

    public ReservationTheme getTheme(ThemeRequest request) {
        validateDuplicate(request.name());

        return new ReservationTheme(null, request.name(), request.description(), request.thumbnail());
    }

    private void validateDuplicate(String name) {
        if (reservationThemeDao.hasSameName(name)) {
            throw new IllegalArgumentException("이름이 동일한 테마가 존재합니다.");
        }
    }

    public void deleteTheme(Long id) {
        if (reservationDao.hasReservationForThemeId(id)) {
            throw new IllegalStateException("예약이 존재하는 테마는 삭제할 수 없습니다.");
        }
        reservationThemeDao.deleteById(id);
    }

    public List<WeeklyThemeResponse> getWeeklyBestThemes() {
        LocalDate now = LocalDate.now();
        LocalDate from = now.minusWeeks(1);
        LocalDate to = now.minusDays(1);

        return reservationThemeDao.findBestThemesInWeek(from, to).stream()
                .map(WeeklyThemeResponse::new)
                .toList();
    }
}
