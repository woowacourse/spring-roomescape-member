package roomescape.reservation.infra;

import java.util.Optional;
import roomescape.reservation.domain.Theme;

import java.time.LocalDate;
import java.util.List;

public interface ThemeRepository {
    Theme save(Theme domain);

    void deleteById(long id);

    List<Theme> findByDate(LocalDate date);

    Optional<Theme> findById(long id);
}
