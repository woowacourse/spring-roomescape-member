package roomescape.console.dao;

import java.util.List;
import roomescape.console.db.InMemoryRoomThemeDb;
import roomescape.dao.RoomThemeDao;
import roomescape.domain.RoomTheme;

public class InMemoryRoomThemeDao implements RoomThemeDao {
    private final InMemoryRoomThemeDb inMemoryRoomThemeDb;

    public InMemoryRoomThemeDao(InMemoryRoomThemeDb inMemoryRoomThemeDb) {
        this.inMemoryRoomThemeDb = inMemoryRoomThemeDb;
    }

    @Override
    public List<RoomTheme> findAll() {
        return inMemoryRoomThemeDb.selectAll();
    }

    @Override
    public List<RoomTheme> findAllRanking() {
        return List.of();
    }

    @Override
    public RoomTheme findById(Long id) {
        return inMemoryRoomThemeDb.selectById(id);
    }

    @Override
    public RoomTheme save(RoomTheme roomTheme) {
        long id = inMemoryRoomThemeDb.insert(roomTheme);
        return roomTheme.withId(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return inMemoryRoomThemeDb.deleteById(id);
    }
}
