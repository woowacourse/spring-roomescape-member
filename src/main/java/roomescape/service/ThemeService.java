package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.dao.reservation.ReservationDao;
import roomescape.dao.theme.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.dto.response.ThemeCreateResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.exception.ExceptionCause;
import roomescape.exception.ReservationExistException;

@Service
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(final ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeResponse> findAll() {
        return themeDao.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public Theme findById(final Long id) {
        Optional<Theme> theme = themeDao.findById(id);
        if (theme.isEmpty()) {
            throw new NoSuchElementException("테마가 존재하지 않습니다.");
        }
        return theme.get();
    }

    public ThemeCreateResponse create(ThemeCreateRequest themeCreateRequest) {
        Theme theme = Theme.create(
                themeCreateRequest.name(),
                themeCreateRequest.description(),
                themeCreateRequest.thumbnail());
        return ThemeCreateResponse.from(themeDao.create(theme));
    }

    public void deleteIfNoReservation(final long id) {
        Theme theme = findById(id);
        if (reservationDao.findByThemeId(id).isPresent()) {
            throw new ReservationExistException(ExceptionCause.RESERVATION_EXIST_THEME);
        }
        themeDao.delete(theme);
    }

    public List<ThemeResponse> findPopularThemesInRecentSevenDays() {
        LocalDate today = LocalDate.now();
        return themeDao.findPopularThemesInRecentSevenDays(today.minusDays(7), today.minusDays(1)).stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
