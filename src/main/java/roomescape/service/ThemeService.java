package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.AvailableReservationResponse;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

@Service
public class ThemeService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ThemeService(ReservationDao reservationDao,
                        ReservationTimeDao reservationTimeDao,
                        ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public ThemeResponse addTheme(ThemeRequest themeRequest) {
        if (themeDao.isExistThemeName(themeRequest.name())) {
            throw new IllegalStateException("이미 존재하는 테마입니다.");
        }
        Theme saved = themeDao.save(themeRequest.toEntity());
        return ThemeResponse.from(saved);
    }

    public List<ThemeResponse> getThemes() {
        return themeDao.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> getTopTenTheme() {
        List<Theme> topTenTheme = themeDao.getPopularThemeByRankAndDuration( // 어제부터 일주일 동안 10등 까지의 테마 추출
                10,
                LocalDate.now().minusDays(7),
                LocalDate.now().minusDays(1)
        );
        return topTenTheme.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void deleteTheme(Long id) {
        if (reservationDao.isExistByThemeId(id)) {
            throw new IllegalStateException("예약이 존재하여 삭제할 수 없습니다.");
        }
        boolean isDeleted = themeDao.deleteById(id);
        if (!isDeleted) {
            throw new IllegalArgumentException("해당하는 ID가 없습니다.");
        }
    }

    public List<AvailableReservationResponse> getThemesTimesWithStatus(Long themeId, LocalDate date) {
        List<ReservationTime> allTimes = reservationTimeDao.findAll();
        List<Reservation> reservations = reservationDao.findByThemeIdAndDate(themeId, date);

        Set<Long> bookedTimeIds = reservations.stream()
                .map(reservation -> reservation.getReservationTime().getId())
                .collect(Collectors.toSet());

        return allTimes.stream()
                .map(time -> new AvailableReservationResponse(
                        time.getId(),
                        time.getStartAt(),
                        bookedTimeIds.contains(time.getId())
                ))
                .toList();
    }
}
