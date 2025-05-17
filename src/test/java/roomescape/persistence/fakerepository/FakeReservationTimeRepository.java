package roomescape.persistence.fakerepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.reservation.ReservationDateTimeFormatter;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.persistence.entity.ReservationTimeEntity;
import roomescape.presentation.member.dto.AvailableTimesResponseDto;

@Repository
public class FakeReservationTimeRepository implements ReservationTimeRepository, FakeRepository {

    private final List<ReservationTimeEntity> reservationTimes = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimes.stream()
                .map(ReservationTimeEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return reservationTimes.stream()
                .filter(time -> Objects.equals(time.getId(), id))
                .findFirst()
                .map(ReservationTimeEntity::toDomain);
    }

    @Override
    public ReservationTime add(ReservationTime reservationTime) {
        ReservationTimeEntity newReservationTimeEntity = ReservationTimeEntity.fromDomain(reservationTime)
                .copyWithId(idGenerator.getAndIncrement());
        reservationTimes.add(newReservationTimeEntity);
        return newReservationTimeEntity.toDomain();
    }

    @Override
    public void deleteById(Long id) {
        reservationTimes.removeIf(reservationTime -> Objects.equals(reservationTime.getId(), id));
    }

    @Override
    public List<AvailableTimesResponseDto> findAvailableTimes(LocalDate date, Long themeId) {
        return List.of();
    }

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        return reservationTimes.stream()
                .anyMatch(reservationTime ->
                        reservationTime.getStartAt()
                                .equals(ReservationDateTimeFormatter.formatTime(startAt))
                );
    }

    @Override
    public void clear() {
        reservationTimes.clear();
        idGenerator.set(1);
    }
}
