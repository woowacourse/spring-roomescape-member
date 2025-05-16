package roomescape.time.repository;

import roomescape.time.entity.ReservationTime;
import roomescape.time.repository.dto.ReservationTimeWithBookedDataResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FakeTimeRepository implements ReservationTimeRepository {
    private final List<ReservationTime> entities = new ArrayList<>();

    @Override
    public ReservationTime save(ReservationTime entity) {
        if (entity.getId() == null) {
            final long nextId = entities.stream()
                    .mapToLong(ReservationTime::getId)
                    .max()
                    .orElse(0) + 1;
            entity = new ReservationTime(
                    nextId,
                    entity.getStartAt()
            );
        }
        entities.add(entity);
        return entity;
    }

    @Override
    public List<ReservationTime> findAll() {
        return Collections.unmodifiableList(entities);
    }

    @Override
    public boolean deleteById(Long id) {
        return entities.removeIf(entity -> entity.getId().equals(id));
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
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
    public Optional<ReservationTime> findByStartAt(LocalTime startAt) {
        return entities.stream()
                .filter(entity -> entity.getStartAt().equals(startAt))
                .findFirst();
    }
}
