package roomescape.domain.reservation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.common.exception.AlreadyInUseException;
import roomescape.domain.reservation.dto.ThemeRequest;
import roomescape.domain.reservation.dto.ThemeResponse;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationDao;
import roomescape.domain.reservation.repository.ThemeDao;

@Service
public class ThemeService {

    private static final int START_DATE_OFFSET = 8;
    private static final int END_DATE_OFFSET = 1;

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(final ThemeDao themeDao, final ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeResponse> getAll() {
        return themeDao.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse create(final ThemeRequest request) {
        Theme theme = themeDao.save(request.toEntity());
        return ThemeResponse.from(theme);
    }

    public void delete(final Long id) {
        if (reservationDao.existsByThemeId(id)) {
            throw new AlreadyInUseException("Theme with id " + id + " not found");
        }

        themeDao.deleteById(id);
    }

    public List<ThemeResponse> getPopularThemes() {
        LocalDate now = LocalDate.now();

        LocalDate startDate = now.minusDays(START_DATE_OFFSET);
        LocalDate endDate = now.minusDays(END_DATE_OFFSET);
        int popularThemeCount = 10;

        return themeDao.findThemeRankingByReservation(startDate, endDate, popularThemeCount)
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
