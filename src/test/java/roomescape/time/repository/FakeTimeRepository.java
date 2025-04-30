package roomescape.time.repository;

import roomescape.time.dto.AvailableReservationTimeResponse;
import roomescape.time.entity.ReservationTimeEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FakeTimeRepository implements ReservationTimeRepository {
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

    @Override
    public List<AvailableReservationTimeResponse> findAvailableTimes(LocalDate date, Long themeId) {
        // TODO: 테스트용 메서드 로직 작성
        return null;
    }
}
