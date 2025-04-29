package roomescape.reservationtime.stub;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

public class StubReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> data = new ArrayList<>();

    public StubReservationTimeRepository(final ReservationTime... times) {
        data.addAll(List.of(times));
    }

    @Override
    public ReservationTime save(final LocalTime startAt) {
        ReservationTime newTime = new ReservationTime(3L, startAt);
        data.add(newTime);
        return newTime;
    }

    @Override
    public List<ReservationTime> findAll() {
        return List.copyOf(data);
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
