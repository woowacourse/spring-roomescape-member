package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain_entity.Theme;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;

@Component
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeResponseDto> findAllThemes() {
        return themeDao.findAll().stream()
                .map(ThemeResponseDto::new)
                .toList();
    }

    public List<ThemeResponseDto> findThemeRank() {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusDays(8);
        LocalDate endDate = now.minusDays(1);
        List<Long> rank = reservationDao.findRank(startDate, endDate);

        return rank.stream().map(themeId -> {
            Theme theme = themeDao.findById(themeId);
            return new ThemeResponseDto(theme);
        }).toList();
    }

    public ThemeResponseDto createTheme(ThemeRequestDto themeRequestDto) {
        Theme themeWithoutId = themeRequestDto.toTheme();
        Long id = themeDao.create(themeWithoutId);
        Theme created = themeWithoutId.copyWithId(id);
        return new ThemeResponseDto(created);
    }

    public void deleteTheme(Long idRequest) {
        themeDao.deleteById(idRequest);
    }
}
