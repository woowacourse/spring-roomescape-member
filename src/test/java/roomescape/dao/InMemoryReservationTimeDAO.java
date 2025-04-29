package roomescape.dao;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.ReservationTime;

public class InMemoryReservationTimeDAO implements ReservationTimeDAO {

    private static final int DUPLICATE_RESERVATION_TIME = -1;

    private final List<ReservationTime> reservationTimes;
    private final AtomicLong index = new AtomicLong(1);

    public InMemoryReservationTimeDAO(final List<ReservationTime> reservationTimes) {
        this.reservationTimes = reservationTimes;
    }

    @Override
    public List<ReservationTime> findAll() {
        return Collections.unmodifiableList(reservationTimes);
    }

    @Override
    public Optional<ReservationTime> findById(final long id) {
        return reservationTimes.stream()
                .filter(reservationTime -> reservationTime.getId() == id)
                .findAny();
    }

    @Override
    public long insert(final ReservationTime reservationTime) {
        if (existsByStartAt(reservationTime.getStartAt())) {
            return DUPLICATE_RESERVATION_TIME;
        }
        long id = index.getAndIncrement();
        reservationTimes.add(reservationTime.withId(id));
        return id;
    }

    @Override
    public boolean existsByStartAt(final LocalTime startAt) {
        return reservationTimes.stream()
                .anyMatch(reservationTime -> reservationTime.getStartAt().equals(startAt));
    }

    @Override
    public boolean deleteById(final long id) {
        ReservationTime target = reservationTimes.stream()
                .filter(reservationTime -> reservationTime.getId() == id)
                .findAny()
                .orElse(null);
        return reservationTimes.remove(target);
    }
}
