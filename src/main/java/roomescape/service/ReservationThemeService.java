package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationThemeDao;
import roomescape.dao.condition.ThemeInsertCondition;
import roomescape.domain.ReservationTheme;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.dto.theme.WeeklyThemeResponse;

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

    public ThemeResponse insertTheme(ThemeRequest themeRequest) {
        validateDuplicate(themeRequest.name());
        ThemeInsertCondition insertCondition = new ThemeInsertCondition(themeRequest.name(),
                themeRequest.description(), themeRequest.thumbnail());
        ReservationTheme inserted = reservationThemeDao.insert(insertCondition);

        return new ThemeResponse(inserted);
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

    private void validateDuplicate(String name) {
        if (reservationThemeDao.hasSameName(name)) {
            throw new IllegalArgumentException("이름이 동일한 테마가 존재합니다.");
        }
    }
}
