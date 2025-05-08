package roomescape.fake;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.business.domain.PlayTime;
import roomescape.persistence.dao.PlayTimeDao;

public class FakePlayTimeDao implements PlayTimeDao {

    private final List<PlayTime> times;
    private final AtomicLong atomicLong = new AtomicLong(1L);

    public FakePlayTimeDao() {
        this.times = new ArrayList<>();
    }

    @Override
    public PlayTime insert(final PlayTime playTime) {
        final Long id = atomicLong.getAndIncrement();
        final PlayTime insertPlayTime = new PlayTime(id, playTime.getStartAt());
        times.add(insertPlayTime);
        return insertPlayTime;
    }

    @Override
    public Optional<PlayTime> findById(final Long id) {
        return times.stream()
                .filter(time -> time.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<PlayTime> findAll() {
        return Collections.unmodifiableList(times);
    }

    @Override
    public boolean deleteById(final Long id) {
        int beforeSize = times.size();
        times.removeIf(time -> time.getId().equals(id));
        int afterSize = times.size();
        int deletedCount = beforeSize - afterSize;
        return deletedCount >= 1;
    }

    @Override
    public boolean existsById(final Long timeId) {
        return times.stream()
                .anyMatch(time -> time.getId().equals(timeId));
    }

    @Override
    public boolean existsByStartAt(final LocalTime startAt) {
        return times.stream()
                .anyMatch(time -> time.getStartAt().equals(startAt));
    }
}
