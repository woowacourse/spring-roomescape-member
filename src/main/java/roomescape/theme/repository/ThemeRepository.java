package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {

    Theme add(Theme theme);

    Theme findByIdOrThrow(Long id);

    void delete(Long id);

    List<Theme> findAll();

    List<Theme> findAllOrderByRank(LocalDate from, LocalDate to, int size);
}
