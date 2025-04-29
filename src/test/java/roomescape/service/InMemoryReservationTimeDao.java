package roomescape.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.resetvationTime.ReservationTimeDao;
import roomescape.domain.ReservationTime;

public class InMemoryReservationTimeDao implements ReservationTimeDao {

    private final AtomicLong index = new AtomicLong(1);
    private final List<ReservationTime> reservationTimes;

    public InMemoryReservationTimeDao(final List<ReservationTime> reservationTimes) {
        this.reservationTimes = reservationTimes;
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimes;
    }

    @Override
    public ReservationTime create(final ReservationTime reservationTime) {
        ReservationTime savedReservationTime = new ReservationTime(index.getAndIncrement(),
                reservationTime.getStartAt());
        reservationTimes.add(savedReservationTime);
        return savedReservationTime;
    }

    @Override
    public boolean deleteIfNoReservation(final long id) {
        ReservationTime reservationTime = reservationTimes.stream()
                .filter(it -> it.isEqualId(id))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        return reservationTimes.remove(reservationTime);
    }

    @Override
    public Optional<ReservationTime> findById(final long id) {
        return reservationTimes.stream()
                .filter(it -> it.isEqualId(id))
                .findFirst();
    }
}
