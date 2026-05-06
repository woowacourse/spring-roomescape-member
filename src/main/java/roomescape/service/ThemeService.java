package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.CreateThemeRequest;
import roomescape.dto.ThemeReservationTimeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

@Service
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public Theme createTheme(CreateThemeRequest request) {
        Long newThemeId = themeDao.save(request);
        return themeDao.findById(newThemeId);
    }

    public void deleteTheme(Long id) {
        themeDao.deleteById(id);
    }

    public List<ThemeReservationTimeResponse> getThemeTimes(Long themeId, LocalDate date) {
        List<Long> reservedTimeIds = reservationDao.findTimeIdsByThemeIdAndDate(themeId, date);
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();

        return reservationTimes.stream()
                .map(time -> new ThemeReservationTimeResponse(
                        time.getId(),
                        time.getStartAt().toString(),
                        reservedTimeIds.contains(time.getId())
                ))
                .toList();
    }
}
