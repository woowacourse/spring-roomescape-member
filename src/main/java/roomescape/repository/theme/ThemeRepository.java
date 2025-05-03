package roomescape.repository.theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    List<Theme> getTopThemesByCount(LocalDate startDate, LocalDate endDate);

    long add(Theme theme);

    void deleteById(long id);
}
