package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.BookedReservationTimeResponseDto;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;

@Service
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;


    public ThemeService(ThemeDao themeDao, ReservationTimeDao reservationTimeDao,
        ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeResponseDto> getAllThemes() {
        return themeDao.findAllTheme().stream().map(ThemeResponseDto::from).toList();
    }

    public ThemeResponseDto saveTheme(ThemeRequestDto request) {
        Theme theme = new Theme(request.name(), request.description(), request.thumbnail());
        long savedId = themeDao.saveTheme(theme);
        theme.setId(savedId);
        return ThemeResponseDto.from(theme);
    }

    public void deleteTheme(Long id) {
        validateIsExistThemeBy(id);
        themeDao.deleteTheme(id);
    }

    public List<BookedReservationTimeResponseDto> getAllBookedReservationTimes(String date,
        Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAllReservationTimes();

        return reservationTimes.stream()
            .map(reservationTime -> createBookedReservationTimeResponseDto(date, themeId,
                reservationTime))
            .toList();
    }

    private BookedReservationTimeResponseDto createBookedReservationTimeResponseDto(
        String date, Long themeId, ReservationTime reservationTime) {
        if (isAlreadyBookedTime(date, themeId, reservationTime)) {
            return BookedReservationTimeResponseDto.from(reservationTime, true);
        }
        return BookedReservationTimeResponseDto.from(reservationTime, false);
    }

    private boolean isAlreadyBookedTime(String date, Long themeId,
        ReservationTime reservationTime) {
        int alreadyExistReservationCount = reservationDao.calculateAlreadyExistReservationBy(
            date, themeId, reservationTime.getId());
        return alreadyExistReservationCount != 0;
    }

    private void validateIsExistThemeBy(Long id) {
        themeDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 테마를 찾을 수 없습니다: " + id));
    }

    public List<ThemeResponseDto> getAllThemeOfRanks() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusDays(7);
        List<Theme> themes = themeDao.findAllThemeOfRanks(startDate, currentDate);
        return themes.stream().map(ThemeResponseDto::from).toList();
    }
}
