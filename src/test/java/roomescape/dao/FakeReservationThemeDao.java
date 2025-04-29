package roomescape.dao;

import roomescape.entity.ReservationThemeEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FakeReservationThemeDao implements ReservationThemeDao {

    private final List<ReservationThemeEntity> entities = new ArrayList<>();

    @Override
    public ReservationThemeEntity save(ReservationThemeEntity entity) {
        entities.add(entity);
        return entity;
    }

    @Override
    public List<ReservationThemeEntity> findAll() {
        return Collections.unmodifiableList(entities);
    }

    @Override
    public boolean deleteById(Long id) {
        return entities.removeIf(entity -> entity.getId().equals(id));
    }

    @Override
    public Optional<ReservationThemeEntity> findById(Long id) {
        return entities.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<ReservationThemeEntity> findByName(String name) {
        return entities.stream()
                .filter(entity -> entity.getName().equals(name))
                .findFirst();
    }
}
