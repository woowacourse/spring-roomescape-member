package roomescape.time.repository;

import roomescape.time.entity.ReservationTimeEntity;
import roomescape.time.repository.ReservationTimeDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FakeTimeDao implements ReservationTimeDao {
    private final List<ReservationTimeEntity> entities = new ArrayList<>();

    @Override
    public ReservationTimeEntity save(ReservationTimeEntity entity) {
        entities.add(entity);
        return entity;
    }

    @Override
    public List<ReservationTimeEntity> findAll() {
        return Collections.unmodifiableList(entities);
    }

    @Override
    public boolean deleteById(Long id) {
        return entities.removeIf(entity -> entity.getId().equals(id));
    }

    @Override
    public Optional<ReservationTimeEntity> findById(Long id) {
        return entities.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }
}
