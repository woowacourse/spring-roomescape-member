package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeSaveRequest;
import roomescape.dto.response.ThemeDeleteResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;

@Service
public class ThemeService {
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(final ThemeDao themeDao, final ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public ThemeResponse save(final ThemeSaveRequest themeSaveRequest) {
        Theme theme = themeSaveRequest.toEntity();
        return ThemeResponse.from(themeDao.save(theme));
    }

    public List<ThemeResponse> getAll() {
        return themeDao.getAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> findThemeRanking() {
        List<Long> themeRankingIds = reservationDao.findRanking(
                LocalDate.now().minusWeeks(1),
                LocalDate.now(),
                10
        );
        return themeRankingIds.stream()
                .map(id -> ThemeResponse.from(themeDao.findById(id).get()))
                .toList();
    }

    public ThemeDeleteResponse delete(final long id) {
        if (themeDao.findById(id).isEmpty()) {
            throw new NoSuchElementException();
        }
        if (!reservationDao.findByThemeId(id).isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 해당 테마를 사용 중인 예약이 있어 삭제할 수 없습니다.");
        }
        return new ThemeDeleteResponse(themeDao.delete(id));
    }
}
