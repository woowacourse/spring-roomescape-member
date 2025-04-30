package roomescape.domain.reservation.repository;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.reservation.entity.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Theme save(Theme Theme);

    void deleteById(Long id);

    Optional<Theme> findById(@NotNull Long id);

    List<Theme> findPopularThemes(LocalDate startDate, LocalDate endDate);
}
