package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

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
        return new ThemeResponse(themeDao.save(theme));
    }

    public List<ThemeResponse> getAll() {
        return themeDao.getAll()
                .stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public List<ThemeResponse> findThemeRanking() {
        List<Long> themeRankingIds = reservationDao.findRanking(
                LocalDate.now().minusWeeks(1),
                LocalDate.now(),
                10
        );
        return themeRankingIds.stream()
                .map(id -> new ThemeResponse(themeDao.findById(id).get()))
                .toList();
    }

    public void delete(final long id) {
        validateDeleteCondition(id);
        themeDao.delete(id);
    }

    private void validateDeleteCondition(long id) {
        validateThemeExist(id);
        validateHasThemeReservation(id);
    }

    private void validateHasThemeReservation(long id) {
        if (!reservationDao.findByThemeId(id).isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 해당 테마를 사용 중인 예약이 있어 삭제할 수 없습니다.");
        }
    }

    private void validateThemeExist(long id) {
        themeDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 잘못된 테마 번호 입력입니다."));
    }
}
