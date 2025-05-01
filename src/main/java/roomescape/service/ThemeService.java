package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain_entity.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;

@Component
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeResponse> findAllThemes() {
        return themeDao.findAll().stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public List<ThemeResponse> findThemeRank() {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusDays(8);
        LocalDate endDate = now.minusDays(1);
        List<Long> rank = reservationDao.findRank(startDate, endDate);

        return rank.stream().map(themeId -> {
            Theme theme = themeDao.findById(themeId);
            return new ThemeResponse(theme);
        }).toList();
    }

    public ThemeResponse createTheme(ThemeRequest themeRequest) {
        Theme themeWithoutId = themeRequest.toTheme();
        Long id = themeDao.create(themeWithoutId);
        Theme created = themeWithoutId.copyWithId(id);
        return new ThemeResponse(created);
    }

    public void deleteTheme(Long idRequest) {
        themeDao.deleteById(idRequest);
    }
}
