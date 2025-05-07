package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    List<Theme> findAllOrderByRank(LocalDate from, LocalDate to, int size);

    Optional<Theme> findById(Long id);

    Theme findByIdOrThrow(Long id);

    Theme add(Theme theme);

    void delete(Long id);
}
