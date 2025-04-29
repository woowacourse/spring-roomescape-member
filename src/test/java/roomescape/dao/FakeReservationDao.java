package roomescape.dao;

import roomescape.entity.ReservationEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FakeReservationDao implements ReservationDao {
    private final List<ReservationEntity> entities = new ArrayList<>();

    @Override
    public ReservationEntity save(ReservationEntity entity) {
        entities.add(entity);
        return entity;
    }

    @Override
    public boolean deleteById(Long id) {
        return entities.removeIf(entity -> entity.getId().equals(id));
    }

    @Override
    public List<ReservationEntity> findAll() {
        return Collections.unmodifiableList(entities);
    }

    @Override
    public List<ReservationEntity> findAllByTimeId(Long id) {
        return entities.stream()
                .filter(reservation -> reservation.getTimeId().equals(id))
                .toList();
    }
}
