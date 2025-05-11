package roomescape.domain.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.dao.ReservationDao;
import roomescape.domain.theme.dao.ThemeDao;
import roomescape.domain.theme.dto.request.ThemeRequestDto;
import roomescape.domain.theme.dto.response.ThemeResponseDto;
import roomescape.domain.theme.model.Theme;
import roomescape.global.exception.InvalidReservationException;

@Service
public class ThemeService {

    private static final int CHECK_STANDARD_OF_DATE = 7;
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeResponseDto> getAllThemes() {
        return themeDao.findAll().stream()
            .map(ThemeResponseDto::from)
            .toList();
    }

    public ThemeResponseDto saveTheme(ThemeRequestDto request) {
        Theme theme = new Theme(request.name(), request.description(), request.thumbnail());
        long savedId = themeDao.save(theme);
        theme.setId(savedId);
        return ThemeResponseDto.from(theme);
    }

    public void deleteTheme(Long id) {
        findById(id);
        if (reservationDao.existReservationByTheme(id)) {
            throw new InvalidReservationException("이미 예약된 테마를 삭제할 수 없습니다.");
        }
        themeDao.delete(id);
    }

    public Theme findById(Long id) {
        return themeDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 테마를 찾을 수 없습니다"));
    }

    public List<ThemeResponseDto> getAllThemeOfRanks() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusDays(CHECK_STANDARD_OF_DATE);
        List<Theme> themes = themeDao.calculateRankForReservationAmount(startDate,
            currentDate);
        return themes.stream().map(ThemeResponseDto::from).toList();
    }
}
