package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.RoomThemeDao;
import roomescape.domain.RoomTheme;
import roomescape.dto.request.RoomThemeRequest;
import roomescape.dto.response.RoomThemeResponse;
import roomescape.exception.TargetNotExistException;

@Service
public class RoomThemeService {
    private final RoomThemeDao roomThemeDao;

    public RoomThemeService(RoomThemeDao roomThemeDao) {
        this.roomThemeDao = roomThemeDao;
    }

    public List<RoomThemeResponse> findAll() {
        return roomThemeDao.findAll()
                .stream()
                .map(RoomThemeResponse::fromRoomTheme)
                .toList();
    }

    public List<RoomThemeResponse> findByRanking() {
        return roomThemeDao.findAllRanking()
                .stream()
                .map(RoomThemeResponse::fromRoomTheme)
                .toList();
    }

    public RoomThemeResponse save(RoomThemeRequest roomThemeRequest) {
        RoomTheme roomTheme = roomThemeRequest.toRoomTheme();
        RoomTheme savedRoomTheme = roomThemeDao.save(roomTheme);
        return RoomThemeResponse.fromRoomTheme(savedRoomTheme);
    }

    public void deleteById(Long id) {
        boolean deleted = roomThemeDao.deleteById(id);
        if (!deleted) {
            throw new TargetNotExistException("삭제할 테마가 존재하지 않습니다.");
        }
    }
}
