package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {

    Theme add(Theme theme);

    Optional<Theme> findById(Long id);

    Theme findByIdOrThrow(Long id);

    void delete(Long id);

    List<Theme> findAll();

    List<Theme> findAllOrderByRank(LocalDate from, LocalDate to, int size);
}
