package roomescape.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface ThemeRepository {

    Theme create(Theme theme);

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    Theme getById(long id) throws NoSuchElementException;

    void deleteById(long id);

    List<Theme> findPopularThemesDateBetween(LocalDate startDate, LocalDate endDate, int limit);
}
