package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.RoomTheme;

public interface RoomThemeDAO {

    long insert(RoomTheme theme);

    boolean existsByName(String name);

    List<RoomTheme> findAll();

    Optional<RoomTheme> findById(long id);

    List<RoomTheme> findPopularThemes(LocalDate start, LocalDate end);

    boolean deleteById(long id);
}
