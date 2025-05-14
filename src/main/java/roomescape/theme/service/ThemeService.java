package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.global.exception.RoomEscapeException.BadRequestException;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.response.AvailableReservationResponse;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.request.AdminThemePageResponse;
import roomescape.theme.dto.request.AdminThemePageResponse.AdminThemePageElementResponse;
import roomescape.theme.dto.request.ThemeRequest;
import roomescape.theme.dto.response.CreateThemeResponse;
import roomescape.theme.dto.response.ThemeResponse;
import roomescape.theme.dto.response.ThemeSelectElementResponse;
import roomescape.theme.repository.ThemeDao;

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

    public CreateThemeResponse addTheme(ThemeRequest themeRequest) {
        if (themeDao.isExistThemeName(themeRequest.name())) {
            throw new BadRequestException("이미 존재하는 테마입니다.");
        }
        Theme saved = themeDao.save(themeRequest.toEntity());
        return CreateThemeResponse.from(saved);
    }

    public AdminThemePageResponse getThemesByPage(int page) {
        int totalThemes = themeDao.countTotalTheme();
        int totalPage = (totalThemes % 10 == 0) ?
                totalThemes / 10 : (totalThemes / 10) + 1;
        if (page < 1 || page > totalPage) {
            throw new ResourceNotFoundException("해당하는 페이지가 없습니다");
        }
        int start = (page - 1) * 10 + 1;
        int end = start + 10 - 1;
        List<AdminThemePageElementResponse> themes = themeDao.findThemesWithPage(start, end).stream()
                .map(AdminThemePageElementResponse::from)
                .toList();
        return new AdminThemePageResponse(totalPage, themes);
    }

    public List<ThemeSelectElementResponse> getAllThemes() {
        return themeDao.findAll()
                .stream()
                .map(ThemeSelectElementResponse::from)
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
            throw new BadRequestException("예약이 존재하여 삭제할 수 없습니다.");
        }
        boolean isDeleted = themeDao.deleteById(id);
        if (!isDeleted) {
            throw new ResourceNotFoundException("해당하는 ID가 없습니다.");
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
