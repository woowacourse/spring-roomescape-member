package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.theme.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.dto.response.ThemeCreateResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.exception.ReservationExistException;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(final ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponse> findAll() {
        return themeDao.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Theme findById(final Long id) {
        final Optional<Theme> theme = themeDao.findById(id);
        if (theme.isEmpty()) {
            throw new NoSuchElementException("테마가 존재하지 않습니다.");
        }
        return theme.get();
    }

    @Transactional
    public ThemeCreateResponse create(final ThemeCreateRequest themeCreateRequest) {
        final Theme theme = new Theme(
                themeCreateRequest.name(),
                themeCreateRequest.description(),
                themeCreateRequest.thumbnail());
        return ThemeCreateResponse.from(themeDao.create(theme));
    }

    @Transactional
    public void deleteIfNoReservation(final long id) {
        if (!themeDao.existsById(id)) {
            throw new NoSuchElementException("테마가 존재하지 않습니다.");
        }
        if (themeDao.deleteIfNoReservation(id)) {
            return;
        }
        throw new ReservationExistException("이 테마에 대한 예약이 존재합니다.");
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> findPopularThemesInRecentSevenDays() {
        final LocalDate today = LocalDate.now();
        return themeDao.findPopularThemesInRecentSevenDays(today.minusDays(7), today.minusDays(1)).stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean existsById(final Long id) {
        return themeDao.existsById(id);
    }
}
