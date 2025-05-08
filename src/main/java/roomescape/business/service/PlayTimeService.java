package roomescape.business.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.business.domain.PlayTime;
import roomescape.exception.DuplicateException;
import roomescape.exception.NotFoundException;
import roomescape.persistence.dao.PlayTimeDao;
import roomescape.presentation.dto.PlayTimeRequest;
import roomescape.presentation.dto.PlayTimeResponse;

@Service
public class PlayTimeService {

    private final PlayTimeDao playTimeDao;

    public PlayTimeService(final PlayTimeDao playTimeDao) {
        this.playTimeDao = playTimeDao;
    }

    public PlayTimeResponse insert(final PlayTimeRequest playTimeRequest) {
        validateStartAtIsNotDuplicate(playTimeRequest.startAt());
        final PlayTime playTime = playTimeRequest.toDomain();
        final Long id = playTimeDao.save(playTime);
        return PlayTimeResponse.withId(id, playTime);
    }

    private void validateStartAtIsNotDuplicate(final LocalTime startAt) {
        if (playTimeDao.existsByStartAt(startAt)) {
            throw new DuplicateException("추가 하려는 시간이 이미 존재합니다.");
        }
    }

    public List<PlayTimeResponse> findAll() {
        return playTimeDao.findAll().stream()
                .map(PlayTimeResponse::from)
                .toList();
    }

    public PlayTime findById(final Long id) {
        return playTimeDao.find(id)
                .orElseThrow(() -> new NotFoundException("해당하는 방탈출 시간을 찾을 수 없습니다. 방탈출 id: %d".formatted(id)));
    }

    public void deleteById(final Long id) {
        if (!playTimeDao.remove(id)) {
            throw new NotFoundException("해당하는 방탈출 시간을 찾을 수 없습니다. 방탈출 id: %d".formatted(id));
        }
    }
}
