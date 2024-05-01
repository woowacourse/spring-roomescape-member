package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationThemeDao;
import roomescape.domain.ReservationTheme;
import roomescape.dto.ReservationThemeRequestDto;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationThemeService {

    private final ReservationDao reservationDao;
    private final ReservationThemeDao reservationThemeDao;

    public ReservationThemeService(ReservationDao reservationDao, ReservationThemeDao reservationThemeDao) {
        this.reservationDao = reservationDao;
        this.reservationThemeDao = reservationThemeDao;
    }
    public List<ReservationTheme> getAllThemes() {
        return reservationThemeDao.findAll();
    }

    public ReservationTheme insertTheme(ReservationThemeRequestDto reservationThemeRequestDto) {
        Long id = reservationThemeDao.insert(reservationThemeRequestDto.name(), reservationThemeRequestDto.description(), reservationThemeRequestDto.thumbnail());
        return new ReservationTheme(id, reservationThemeRequestDto.name(), reservationThemeRequestDto.description(), reservationThemeRequestDto.thumbnail());
    }

    public void deleteTheme(Long id) {
        if (reservationDao.countByThemeId(id) != 0) {
            throw new IllegalStateException();
        }
        reservationThemeDao.deleteById(id);
    }

    public List<ReservationTheme> getWeeklyBestThemes() {
        LocalDate now = LocalDate.now();
        LocalDate from = now.minusWeeks(1);
        LocalDate to = now.minusDays(1);

        List<Long> bestTheme = reservationDao.findBestThemeIdInWeek(from.toString(), to.toString());
        return bestTheme.stream()
                .map(themeId -> reservationThemeDao.findById(themeId)
                        .orElseThrow(IllegalArgumentException::new))
                .limit(10)
                .toList();
    }
}
