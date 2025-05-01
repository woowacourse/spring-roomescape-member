package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.service.reservation.Theme;
import roomescape.service.reservation.ThemeName;

public interface ThemeDao {

    boolean isExists(ThemeName name);

    Theme save(Theme theme);

    List<Theme> findAll();

    Theme findById(long id);

    void deleteById(long id);

    List<Theme> findPopularThemes(LocalDate from, LocalDate to, int count);

    boolean isNotExistsById(long id);
}
