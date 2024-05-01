package roomescape.console.db;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.RoomTheme;

public class InMemoryRoomThemeDb {

    private Map<Long, RoomTheme> roomThemes = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong(1);

    //TODO: refactor
    public long insert(RoomTheme roomTheme) {
        long thisId = id.getAndIncrement();
        roomThemes.put(thisId, roomTheme.withId(thisId));
        return thisId;
    }

    public List<RoomTheme> selectAll() {
        return roomThemes.values().stream().toList();
    }

    public RoomTheme selectById(Long id) {
        return roomThemes.get(id);
    }

    public boolean deleteById(Long id) {
        boolean exists = roomThemes.containsKey(id);
        if(exists) {
            roomThemes.remove(id);
        }
        return exists;
    }
}
