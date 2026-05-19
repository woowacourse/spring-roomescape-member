package roomescape.fake;

import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FakeReservationTimeRepository implements ReservationTimeRepository {
    private final List<ReservationTime> times;

    public FakeReservationTimeRepository(List<ReservationTime> times) {
        this.times = new ArrayList<>(times);
    }

    @Override
    public ReservationTime addTime(ReservationTime reservationTime) {
        times.add(reservationTime);
        return reservationTime;
    }

    @Override
    public List<ReservationTime> findAllReservationTimes() {
        return List.copyOf(times);
    }

    @Override
    public void deleteTime(Long id) {
        times.removeIf(t -> Objects.equals(t.id(), id));
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return times.stream()
                .filter(t -> Objects.equals(t.id(), id))
                .findFirst();
    }

    @Override
    public List<ReservationTime> findAvailableTimes(Long themeId, LocalDate date) {
        throw new UnsupportedOperationException();
    }
}
