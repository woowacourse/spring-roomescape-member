package roomescape.reservationtime.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservationtime.ReservationTime;

public class FakeReservationTimeDao implements ReservationTimeDao {

    private final List<ReservationTime> fakeReservationTimes = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    public FakeReservationTimeDao(ReservationTime... reservationTimes) {
        Arrays.stream(reservationTimes)
                .forEach(reservationTime -> fakeReservationTimes.add(reservationTime));
    }

    @Override
    public List<ReservationTime> findAll() {
        return new ArrayList<>(fakeReservationTimes);
    }

    @Override
    public Long create(ReservationTime reservationTime) {
        ReservationTime reservationTimeWithId = new ReservationTime(index.getAndIncrement(), reservationTime.getStartAt());
        fakeReservationTimes.add(reservationTime);
        return reservationTimeWithId.getId();
    }

    @Override
    public int delete(Long id) {
        fakeReservationTimes.removeIf(reservationTime -> reservationTime.getId().equals(id));
        return fakeReservationTimes.size();
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return Optional.ofNullable(fakeReservationTimes.stream()
                .filter(reservationTime -> reservationTime.getId().equals(id))
                .findFirst()
                .orElseThrow(NoSuchElementException::new));
    }
}
