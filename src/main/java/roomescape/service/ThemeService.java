package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;

import java.util.Set;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.PopularThemeResponse;
import roomescape.dto.ReservationTimeStatusResponse;

@Service
public class ThemeService {

    private static final int POPULAR_PERIOD_START = 7;
    private static final int POPULAR_PERIOD_END = 1;
    private final ThemeDao themeDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationTimeDao reservationTimeDao,
                        ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public List<PopularThemeResponse> findPopularThemes() {
        LocalDate startDate = LocalDate.now().minusDays(POPULAR_PERIOD_START);
        LocalDate endDate = LocalDate.now().minusDays(POPULAR_PERIOD_END);
        return themeDao.findPopularThemes(startDate, endDate)
                .stream()
                .map(popularTheme -> PopularThemeResponse.of(
                        popularTheme.theme(),
                        popularTheme.reservationCount())
                )
                .toList();
    }

    public Theme save(Theme theme) {
        if (themeDao.existsByName(theme.getName())) {
            throw new IllegalArgumentException("이미 존재하는 테마 이름입니다.");
        }
        return themeDao.save(theme);
    }

    public void deleteById(Long id) {
        if (reservationDao.existByThemeId(id)) {
            throw new IllegalArgumentException("기존 예약이 존재하는 테마는 삭제할 수 없습니다.");
        }
        themeDao.deleteById(id);
    }

    public List<ReservationTimeStatusResponse> findReservationTimeByDateAndThemeId(LocalDate date, Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        Set<Long> reservedTimeIds = new HashSet<>(reservationDao.findReservedTimeIdsByDateAndThemeId(date, themeId));
        return reservationTimes.stream()
                .map(reservationTime -> {
                    boolean available = isAvailable(reservationTime, reservedTimeIds, date);
                    return ReservationTimeStatusResponse.of(reservationTime, available);
                })
                .toList();
    }

    private boolean isAvailable(ReservationTime reservationTime, Set<Long> reservedTimeIds, LocalDate date) {
        return !reservedTimeIds.contains(reservationTime.getId()) && !isPastDateTime(date, reservationTime);
    }

    private boolean isPastDateTime(LocalDate date, ReservationTime reservationTime) {
        return date.isBefore(LocalDate.now()) || isTodayAndPastTime(date, reservationTime);
    }

    private boolean isTodayAndPastTime(LocalDate date, ReservationTime reservationTime) {
        return date.isEqual(LocalDate.now()) && reservationTime.getStartAt()
                .isBefore(LocalTime.now());
    }
}
