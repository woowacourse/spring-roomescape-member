package roomescape.stub;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.AvailableReservationTimeResponse;
import roomescape.repository.ReservationTimeRepository;

public class StubReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> data = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong();

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
        return List.of();  // TODO. Stub이기 때문에 Setter로 구현하는 방식을 어떻게 생각하시는지 지노에게 여쭤보기
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
