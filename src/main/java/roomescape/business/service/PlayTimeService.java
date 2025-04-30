package roomescape.business.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.business.domain.PlayTime;
import roomescape.persistence.dao.PlayTimeDao;
import roomescape.exception.PlayTimeNotFoundException;
import roomescape.presentation.dto.PlayTimeRequest;
import roomescape.presentation.dto.PlayTimeResponse;

@Service
public class PlayTimeService {

    private final PlayTimeDao playTimeDao;

    public PlayTimeService(final PlayTimeDao playTimeDao) {
        this.playTimeDao = playTimeDao;
    }

    public PlayTime find(final Long id) {
        return playTimeDao.find(id)
                .orElseThrow(() -> new PlayTimeNotFoundException(id));
    }

    public PlayTimeResponse create(final PlayTimeRequest playTimeRequest) {
        final PlayTime playTime = playTimeRequest.toDomain();
        final Long id = playTimeDao.save(playTime);

        return PlayTimeResponse.withId(id, playTime);
    }

    public List<PlayTimeResponse> findAll() {
        return playTimeDao.findAll().stream()
                .map(PlayTimeResponse::from)
                .toList();
    }

    public void remove(final Long id) {
        if (!playTimeDao.remove(id)) {
            throw new PlayTimeNotFoundException(id);
        }
    }
}
