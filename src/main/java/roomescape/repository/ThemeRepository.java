package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.exception.RoomEscapeException;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ThemeRepository {

    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;

    public ThemeRepository(ReservationDao reservationDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
    }

    public List<Theme> findBest10Between(LocalDate startDate, LocalDate endDate) {
        List<Long> themeIds = reservationDao.find10ThemeIdsOrderByReservationThemeCountDescBetween(startDate, endDate);
        return themeIds.stream()
                .map(this::getTheme)
                .toList();
    }

    private Theme getTheme(Long id) {
        return themeDao.findById(id)
                .orElseThrow(() -> new RoomEscapeException("[ERROR] 테마를 찾을 수 없습니다."));
    }
}
