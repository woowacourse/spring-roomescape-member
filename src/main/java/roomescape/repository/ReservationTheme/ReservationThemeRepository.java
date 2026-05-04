package roomescape.repository.ReservationTheme;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.dao.ReservationThemeDao;
import roomescape.domain.Theme.Theme;
import roomescape.domain.Theme.ThemeCommand;
import roomescape.domain.Theme.ThemeDaoData;

@Repository
public class ReservationThemeRepository {
    private final ReservationThemeDao reservationThemeDao;

    public ReservationThemeRepository(ReservationThemeDao reservationThemeDao) {
        this.reservationThemeDao = reservationThemeDao;
    }

    public Theme addTheme(ThemeCommand themeCommand) {
        ThemeDaoData themeDaoData = reservationThemeDao.addTheme(themeCommand);
        return createTheme(themeDaoData);
    }

    public List<Theme> getAllTheme() {
        List<ThemeDaoData> themeDaoAllData = reservationThemeDao.getAllTheme();
        return themeDaoAllData.stream()
                .map(this::createTheme)
                .toList();
    }

    public Optional<Theme> getTheme(long id) {
        Optional<ThemeDaoData> themeDaoData = reservationThemeDao.getTheme(id);

        if(themeDaoData.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(createTheme(reservationThemeDao.getTheme(id).get()));
    }

    public void deleteTheme(long id) {
        reservationThemeDao.deleteTheme(id);
    }

    private Theme createTheme(ThemeDaoData themeDaoData) {
        return new Theme(
                themeDaoData.id(),
                themeDaoData.name(),
                themeDaoData.description(),
                themeDaoData.imageUrl()
        );
    }
}
