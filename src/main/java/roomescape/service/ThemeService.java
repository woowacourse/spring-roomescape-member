package roomescape.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.domain.exception.IllegalRequestArgumentException;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.dto.response.ThemeResponse;

@Service
public class ThemeService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int MAX_DAYS_TO_SUBTRACT = 7;
    private static final int MIN_DAYS_TO_SUBTRACT = 1;

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeResponse> readThemes() {
        return themeDao.readThemes().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> readPopularThemes() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusDays(MAX_DAYS_TO_SUBTRACT);
        LocalDate endDate = currentDate.minusDays(MIN_DAYS_TO_SUBTRACT);
        return themeDao.readThemesRankingOfReservation(startDate.format(DATE_FORMATTER), endDate.format(DATE_FORMATTER))
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse createTheme(ThemeCreateRequest dto) {
        if (themeDao.existsThemeByName(dto.name())) {
            throw new IllegalRequestArgumentException("해당 테마 이름은 이미 존재합니다.");
        }
        Theme createdTheme = themeDao.createTheme(dto.createTheme());
        return ThemeResponse.from(createdTheme);
    }

    public void deleteTheme(Long id) {
        if (reservationDao.existsReservationByThemeId(id)) {
            throw new IllegalRequestArgumentException("해당 테마를 사용하는 예약이 존재합니다.");
        }
        themeDao.deleteTheme(id);
    }
}
