package roomescape.fake;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import roomescape.business.domain.PlayTime;
import roomescape.persistence.dao.PlayTimeDao;
import roomescape.persistence.entity.PlayTimeEntity;

public class FakePlayTimeDao implements PlayTimeDao {

    private final List<PlayTimeEntity> times;

    private int index = 1;

    public FakePlayTimeDao() {
        this.times = new ArrayList<>();
        final PlayTimeEntity dummy = new PlayTimeEntity(null, null);
        times.add(dummy);
    }

    public FakePlayTimeDao(final List<PlayTimeEntity> times) {
        this.times = times;
        index += times.size();
        final PlayTimeEntity dummy = new PlayTimeEntity(null, null);
        times.addFirst(dummy);
    }

    @Override
    public Long save(final PlayTime playTime) {
        final PlayTimeEntity temp = PlayTimeEntity.from(playTime);
        final PlayTimeEntity playTimeEntity = new PlayTimeEntity(
                (long) index,
                temp.startAt()
        );
        times.add(index, playTimeEntity);

        return (long) index++;
    }

    @Override
    public Optional<PlayTime> find(final Long id) {
        try {
            final PlayTimeEntity playTimeEntity = times.get(Math.toIntExact(id));
            return Optional.of(playTimeEntity.toDomain());
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<PlayTime> findAll() {
        return times.stream()
                .filter(timeEntity -> timeEntity.id() != null)
                .map(PlayTimeEntity::toDomain)
                .toList();
    }

    @Override
    public boolean remove(final Long id) {
        try {
            times.remove(times.get(Math.toIntExact(id)));
            index--;
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean existsByStartAt(final LocalTime startAt) {
        final String rawStartAt = PlayTimeEntity.formatStartAt(startAt);

        return times.stream()
                .filter(timeEntity -> timeEntity.id() != null)
                .anyMatch(timeEntity -> timeEntity.startAt().equals(rawStartAt));
    }

    public List<PlayTimeEntity> getTimes() {
        return times;
    }
}
