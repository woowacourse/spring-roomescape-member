package roomescape.dao;

import java.util.List;
import roomescape.domain.RoomTheme;

public interface RoomThemeDao {
    List<RoomTheme> findAll();

    RoomTheme findById(Long id);

    RoomTheme save(RoomTheme roomTheme);

    boolean deleteById(Long id);
}
