package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.theme.Theme;
import roomescape.dto.theme.ThemeCreateRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.exception.InvalidValueException;

@Service
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeResponse> findAll() {
        return themeDao.readAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> findPopulars(LocalDate today) {
        List<Long> themeIds =
                reservationDao.readPopularThemeIds(today.minusWeeks(1), today.minusDays(1));
        return themeIds.stream()
                .map(this::findTheme)
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse add(ThemeCreateRequest request) {
        Theme theme = request.toDomain();
        Theme result = themeDao.create(theme);
        return ThemeResponse.from(result);
    }

    public void delete(Long id) {
        validateNotExistTheme(id);
        validateExistReservationByThemeId(id);
        themeDao.delete(id);
    }

    private Theme findTheme(Long themeId) {
        return themeDao.readById(themeId)
                .orElseThrow(() -> new InvalidValueException("존재하지 않는 테마입니다."));
    }

    private void validateNotExistTheme(Long id) {
        if (!themeDao.exist(id)) {
            throw new InvalidValueException("테마 아이디에 해당하는 테마가 존재하지 않습니다.");
        }
    }

    private void validateExistReservationByThemeId(Long id) {
        if (reservationDao.existByThemeId(id)) {
            throw new InvalidValueException("해당 테마를 사용하는 예약이 존재합니다.");
        }
    }
}
