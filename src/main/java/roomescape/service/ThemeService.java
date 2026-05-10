package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.PopularThemeResponse;
import roomescape.dto.ReservationTimeStatusResponse;

@Service
public class ThemeService {

    private static final int POPULAR_THEME_LIMIT = 10;

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public List<PopularThemeResponse> findPopularThemes() {
        LocalDate today = LocalDate.now();
        return findPopularThemes(today.minusDays(7), today.minusDays(1), POPULAR_THEME_LIMIT);
    }

    public List<PopularThemeResponse> findPopularThemes(LocalDate startDate, LocalDate endDate, int limit) {
        return themeDao.findPopularThemes(startDate, endDate, limit)
                .stream()
                .map(result -> PopularThemeResponse.of(
                        result.theme(),
                        result.reservationCount()
                ))
                .toList();
    }

    public Theme save(Theme theme) {
        if (themeDao.existsByName(theme.getName())) {
            throw new IllegalArgumentException("이미 존재하는 테마 이름입니다.");
        }
        return themeDao.save(theme);
    }

    public void deleteById(Long id) {
        if (reservationDao.existByThemeId(id)) {
            throw new IllegalArgumentException("기존 예약이 존재하는 테마는 삭제할 수 없습니다.");
        }
        themeDao.deleteById(id);
    }
}
