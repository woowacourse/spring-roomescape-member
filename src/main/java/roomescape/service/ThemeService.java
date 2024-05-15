package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.theme.Theme;
import roomescape.dto.theme.ThemeCreateRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.service.exception.InvalidRequestException;

@Service
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;
    private final Clock clock;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao, Clock clock) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
        this.clock = clock;
    }

    public List<ThemeResponse> findAll() {
        return themeDao.readAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> findPopulars() {
        LocalDate today = LocalDate.now(clock);
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
        validateNull(id);
        validateNotExistTheme(id);
        validateExistReservationByThemeId(id);
        themeDao.delete(id);
    }

    private Theme findTheme(Long themeId) {
        return themeDao.readById(themeId)
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 테마입니다."));
    }

    private void validateNull(Long id) {
        if (id == null) {
            throw new InvalidRequestException("테마 아이디가 없습니다.");
        }
    }

    private void validateNotExistTheme(Long id) {
        if (!themeDao.exist(id)) {
            throw new InvalidRequestException("테마 아이디에 해당하는 테마가 존재하지 않습니다.");
        }
    }

    private void validateExistReservationByThemeId(Long id) {
        if (reservationDao.existByThemeId(id)) {
            throw new InvalidRequestException("해당 테마를 사용하는 예약이 존재합니다.");
        }
    }
}
