package roomescape.theme.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.exception.ExistedThemeException;
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

    public ThemeResponse create(ThemeRequest themeRequest) {
        Optional<Theme> optionalTheme = themeDao.findByName(themeRequest.name());
        if (optionalTheme.isPresent()) {
            throw new ExistedThemeException();
        }

        Theme theme = Theme.createWithoutId(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        Theme themeWithId = themeDao.create(theme);
        return new ThemeResponse(themeWithId.getId(), themeWithId.getName(), themeWithId.getDescription(),
                themeWithId.getThumbnail());
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
        LocalDate startDate = LocalDate.now().minusDays(8);
        Map<Theme, Long> themeCounts = reservationDao.findAll().stream()
                .filter(reservation -> reservation.getDate().isBefore(LocalDate.now()))
                .filter(reservation -> reservation.getDate().isAfter(startDate))
                .collect(Collectors.groupingBy(reservation -> reservation.getTheme(), Collectors.counting()));

        return themeCounts.keySet().stream().sorted(new Comparator<Theme>() {
                    @Override
                    public int compare(Theme o1, Theme o2) {
                        return (int) (themeCounts.get(o1) - themeCounts.get(o2));
                    }
                }).map(theme -> new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail()))
                .toList();
    }
}
