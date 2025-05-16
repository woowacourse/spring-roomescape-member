package roomescape.stub;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Setter;
import roomescape.domain.entity.ReservationTime;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.dto.response.AvailableReservationTimeResponse;

public class StubReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> data = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong();
    @Setter
    private Set<Long> bookedTimeIds = Set.of();

    public StubReservationTimeRepository(ReservationTime... initialReservationTimes) {
        data.addAll(List.of(initialReservationTimes));
        long maxId = data.stream()
                .mapToLong(ReservationTime::getId)
                .max()
                .orElse(0L);
        idSequence.set(maxId);
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        ReservationTime saved = new ReservationTime(idSequence.incrementAndGet(), reservationTime.getStartAt());
        data.add(saved);
        return saved;
    }

    @Override
    public List<ReservationTime> findAll() {
        return List.copyOf(data);
    }

    @Override
    public List<AvailableReservationTimeResponse> findAllAvailable(LocalDate date, Long themeId) {
        return data.stream()
                .map(time -> new AvailableReservationTimeResponse(time.getId(), time.getStartAt(),
                        bookedTimeIds.contains(time.getId())))
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(time -> time.getId().equals(id));
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return data.stream()
                .filter(time -> time.getId().equals(id))
                .findFirst();
    }
}
