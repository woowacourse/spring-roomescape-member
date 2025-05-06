package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.RoomTheme;

public interface RoomThemeRepository {

    long insert(RoomTheme theme);

    boolean existsByName(String name);

    List<RoomTheme> findAll();

    Optional<RoomTheme> findById(long id);

    List<RoomTheme> findPopularThemes(LocalDate start, LocalDate end, int topLimit);

    boolean deleteById(long id);
}
