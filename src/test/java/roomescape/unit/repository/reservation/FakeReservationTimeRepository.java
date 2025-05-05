package roomescape.unit.repository.reservation;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.reservation.ReservationTime;
import roomescape.repository.reservation.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    AtomicLong index = new AtomicLong(1L);
    List<ReservationTime> reservationTimes = new ArrayList<>();

    @Override
    public long add(ReservationTime reservationTime) {
        long id = index.getAndIncrement();
        reservationTimes.add(new ReservationTime(id, reservationTime.getTime()));
        return id;
    }

    @Override
    public List<ReservationTime> findAll() {
        return Collections.unmodifiableList(reservationTimes);
    }

    @Override
    public void deleteById(Long id) {
        Optional<ReservationTime> findReservationTimes = reservationTimes.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findAny();
        findReservationTimes.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id입니다." + id));
        reservationTimes.remove(findReservationTimes.get());
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return reservationTimes.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findAny();
    }

    @Override
    public boolean existsByTime(LocalTime time) {
        return reservationTimes.stream()
                .anyMatch((reservationTime) -> reservationTime.getTime().equals(time));
    }
}
