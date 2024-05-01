package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.RoomThemeDao;
import roomescape.domain.RoomTheme;
import roomescape.dto.request.RoomThemeCreateRequest;
import roomescape.dto.response.RoomThemeResponse;

@Service
public class RoomThemeService {
    private final RoomThemeDao roomThemeDao;

    public RoomThemeService(RoomThemeDao roomThemeDao) {
        this.roomThemeDao = roomThemeDao;
    }

    public RoomThemeResponse save(RoomThemeCreateRequest roomThemeCreateRequest) {
        RoomTheme roomTheme = roomThemeCreateRequest.toRoomTheme();
        RoomTheme savedRoomTheme = roomThemeDao.save(roomTheme);
        return RoomThemeResponse.fromRoomTheme(savedRoomTheme);
    }

    public List<RoomThemeResponse> findAll() {
        return roomThemeDao.findAll()
                .stream()
                .map(RoomThemeResponse::fromRoomTheme)
                .toList();
    }

    public boolean deleteById(Long id) {
        return roomThemeDao.deleteById(id);
    }
}
