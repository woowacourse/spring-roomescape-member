package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(final ThemeDao themeDao, final ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public ThemeResponse save(final ThemeRequest themeRequest) {
        Theme theme = themeRequest.toEntity();
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

    public void delete(final long id) {
        themeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 테마 번호 입력입니다."));

        if (!reservationDao.findByThemeId(id).isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 해당 테마를 사용 중인 예약이 있어 삭제할 수 없습니다.");
        }
        themeDao.delete(id);
    }
}
