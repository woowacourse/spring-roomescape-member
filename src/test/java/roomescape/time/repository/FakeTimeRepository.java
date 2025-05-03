package roomescape.time.repository;

import roomescape.time.entity.ReservationTimeEntity;
import roomescape.time.repository.dto.ReservationTimeWithBookedDataResponse;

import java.time.LocalDate;
import java.time.LocalTime;
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
    public List<ReservationTimeWithBookedDataResponse> findAllWithBooked(LocalDate date, Long themeId) {
        // TODO: fake 코드 작성하기
        return null;
    }

    @Override
    public Optional<ReservationTimeEntity> findByStartAt(LocalTime startAt) {
        return entities.stream()
                .filter(entity -> entity.getStartAt().equals(startAt))
                .findFirst();
    }
}
