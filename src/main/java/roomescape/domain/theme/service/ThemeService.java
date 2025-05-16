package roomescape.domain.theme.service;

import static roomescape.global.exception.ErrorMessage.ALREADY_USED_RESOURCE;
import static roomescape.global.exception.ErrorMessage.NOT_FOUND_ID;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.dao.ReservationDao;
import roomescape.domain.theme.dao.ThemeDao;
import roomescape.domain.theme.dto.request.ThemeRequestDto;
import roomescape.domain.theme.dto.response.ThemeResponseDto;
import roomescape.domain.theme.model.Theme;
import roomescape.global.exception.InvalidReservationException;
import roomescape.global.exception.NotFoundException;

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
        if (reservationDao.existReservationByTheme(id)) {
            throw new InvalidReservationException(ALREADY_USED_RESOURCE);
        }

        boolean isDeleted = themeDao.delete(id);
        if (!isDeleted) {
            throw new NotFoundException(NOT_FOUND_ID);
        }
    }

    public Theme findById(Long id) {
        return themeDao.findById(id)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_ID));
    }

    public List<ThemeResponseDto> getAllThemeOfRanks() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusDays(CHECK_STANDARD_OF_DATE);
        List<Theme> themes = themeDao.calculateRankForReservationAmount(startDate,
            currentDate);
        return themes.stream().map(ThemeResponseDto::from).toList();
    }
}
