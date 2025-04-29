package roomescape.repository;

import roomescape.domain.ReservationTime;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> reservationTimes;
    private final AtomicLong reservationTimeId = new AtomicLong(1);

    public FakeReservationTimeRepository(final List<ReservationTime> reservationTimes) {
        this.reservationTimes = reservationTimes;
    }

    @Override
    public Optional<ReservationTime> save(ReservationTime reservationTime) {
        ReservationTime newReservationTime = new ReservationTime(reservationTimeId.getAndIncrement(), reservationTime.startAt());
        reservationTimes.add(newReservationTime);
        return findById(newReservationTime.id());
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimes;
    }

    @Override
    public int deleteById(long id) {
        ReservationTime deleteReservation = reservationTimes.stream()
                .filter(reservationTime -> Objects.equals(reservationTime.id(), id))
                .findFirst().orElse(null);

        if (deleteReservation != null) {
            int affectedRows = (int) reservationTimes.stream()
                    .filter(reservationTime -> Objects.equals(reservationTime.id(), id))
                    .count();
            reservationTimes.remove(deleteReservation);
            return affectedRows;
        }
        return 0;
    }

    @Override
    public Optional<ReservationTime> findById(long id) {
        return reservationTimes.stream()
                .filter(reservationTime -> Objects.equals(reservationTime.id(), id))
                .findFirst();
    }
}
