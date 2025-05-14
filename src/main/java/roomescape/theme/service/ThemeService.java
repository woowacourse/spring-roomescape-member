package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.exception.ExistedException;
import roomescape.reservation.dao.ReservationDao;
import roomescape.theme.Theme;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.dto.request.ThemeRequest;
import roomescape.theme.dto.response.ThemeResponse;

@Service
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public Long create(ThemeRequest themeRequest) {
        Optional<Theme> optionalTheme = themeDao.findByName(themeRequest.name());
        if (optionalTheme.isPresent()) {
            throw new ExistedException("테마가 이미 존재합니다.");
        }

        Theme theme = Theme.createWithoutId(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        return themeDao.create(theme);
    }

    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeDao.findAll();
        return themes.stream()
                .map(theme ->
                        new ThemeResponse(
                                theme.getId(),
                                theme.getName(),
                                theme.getDescription(),
                                theme.getThumbnail()
                        )
                )
                .toList();
    }

    public void delete(long id) {
        themeDao.delete(id);
    }

    public List<ThemeResponse> getTop10Themes() {
        LocalDate currentDate = LocalDate.now();
        List<Theme> top10Themes = reservationDao.findTop10Themes(currentDate);

        return top10Themes.stream()
                .map(theme -> new ThemeResponse(
                        theme.getId(),
                        theme.getName(),
                        theme.getDescription(),
                        theme.getThumbnail()
                ))
                .toList();
    }
}
