package roomescape.stub;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.AvailableReservationTimeResponse;
import roomescape.repository.ReservationTimeRepository;

public class StubReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> data = new ArrayList<>();

    public StubReservationTimeRepository(ReservationTime... times) {
        data.addAll(List.of(times));
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        ReservationTime newTime = new ReservationTime(3L, reservationTime.getStartAt());
        data.add(newTime);
        return newTime;
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
