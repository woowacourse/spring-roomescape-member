package roomescape.reservation.repository;

import roomescape.reservation.entity.ReservationEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FakeReservationRepository implements ReservationRepository {
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

    @Override
    public Optional<ReservationEntity> findDuplicatedWith(ReservationEntity other) {
        return entities.stream()
                .filter(entity -> entity.getDate().isEqual(other.getDate())
                        && entity.getStartAt().equals(other.getStartAt()))
                .findFirst();
    }
}
