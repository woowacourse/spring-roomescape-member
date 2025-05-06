package roomescape.stub;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.repository.ReservationTimeRepository;

public class StubReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> data = new ArrayList<>();

    public StubReservationTimeRepository(final ReservationTime... times) {
        data.addAll(List.of(times));
    }

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        ReservationTime newTime = new ReservationTime(3L, reservationTime.getStartAt());
        data.add(newTime);
        return newTime;
    }

    @Override
    public List<ReservationTime> findAll() {
        return List.copyOf(data);
    }

    @Override
    public List<AvailableReservationTimeResponse> findAllAvailable(final LocalDate date, final Long themeId) {
        return List.of();
    }

    @Override
    public void deleteById(final Long id) {
        data.removeIf(time -> time.getId().equals(id));
    }

    @Override
    public Optional<ReservationTime> findById(final Long id) {
        return data.stream()
                .filter(time -> time.getId().equals(id))
                .findFirst();
    }
}
