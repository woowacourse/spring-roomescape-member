package roomescape.repository.theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    List<Theme> getTopThemes(LocalDate startDate, LocalDate endDate, int count);

    long add(Theme theme);

    void deleteById(long id);
}
