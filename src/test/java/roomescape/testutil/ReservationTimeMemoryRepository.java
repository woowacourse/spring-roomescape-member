package roomescape.testutil;

import roomescape.model.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationTimeMemoryRepository implements ReservationTimeRepository {

    private final AtomicLong reservationTimeId = new AtomicLong(1);
    private final List<ReservationTime> reservationTimes = new ArrayList<>();

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        final Long savedReservationTimeId = reservationTimeId.getAndIncrement();
        final ReservationTime savedReservationTime = new ReservationTime(savedReservationTimeId, reservationTime.getStartAt());
        reservationTimes.add(savedReservationTime);
        return savedReservationTime;
    }

    @Override
    public Optional<ReservationTime> findById(final Long id) {
        return reservationTimes.stream()
                .filter(reservationTime -> reservationTime.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<ReservationTime> findAll() {
        return Collections.unmodifiableList(reservationTimes);
    }

    @Override
    public void deleteById(final Long id) {
        reservationTimes.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst()
                .ifPresent(reservationTimes::remove);
    }
}
