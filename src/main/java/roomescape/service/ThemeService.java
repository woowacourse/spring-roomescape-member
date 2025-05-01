package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.model.Theme;

@Service
public class ThemeService {
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final Clock clock;

    public ThemeService(ReservationDao reservationDao, ThemeDao themeDao, Clock clock) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.clock = clock;
    }

    public ThemeResponse addTheme(ThemeRequest themeRequest) {
        if (themeDao.isExistThemeName(themeRequest.name())) {
            throw new IllegalStateException("이미 존재하는 테마입니다.");
        }
        Theme saved = themeDao.save(themeRequest.toEntity());
        return ThemeResponse.from(saved);
    }

    public List<ThemeResponse> getThemes() {
        return themeDao.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> getTopTenTheme() {
        List<Theme> topTenTheme = themeDao.getPopularThemeByRankAndDuration(
                10,
                LocalDate.now(clock).minusDays(7),
                LocalDate.now(clock).minusDays(1)
        );
        return topTenTheme.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void deleteTheme(Long id) {
        if (reservationDao.isExistByThemeId(id)) {
            throw new IllegalStateException("예약이 존재하여 삭제할 수 없습니다.");
        }
        boolean isDeleted = themeDao.deleteById(id);
        if (!isDeleted) {
            throw new IllegalArgumentException("해당하는 ID가 없습니다.");
        }
    }
}
