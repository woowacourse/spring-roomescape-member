package roomescape.service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationDao;

public class FakeReservationDao implements ReservationDao {

    Map<Long, Reservation> reservations;
    AtomicLong reservationAtomicLong = new AtomicLong(0);
    AtomicLong reservationTimeAtomicLong = new AtomicLong(0);

    FakeReservationDao() {
        this.reservations = new HashMap<>();
    }

    FakeReservationDao(List<Reservation> reservations) {
        this.reservations = new HashMap<>();
        for (int i = 0; i < reservations.size(); i++) {
            this.reservations.put(reservations.get(i).getId(), reservations.get(i));
        }
    }

    @Override
    public List<Reservation> findAll() {
        return reservations.values().stream().toList();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        if (reservations.containsKey(id)) {
            return Optional.of(reservations.get(id));
        }
        return Optional.empty();
    }

    @Override
    public Reservation insert(Reservation reservationAddRequest) {
        Long id = reservationAtomicLong.incrementAndGet();
        Reservation reservation = new Reservation(id, reservationAddRequest.getName(), reservationAddRequest.getDate(),
                new ReservationTime(reservationTimeAtomicLong.incrementAndGet(),
                        LocalTime.of(10, 0)));
        reservations.put(id, reservation);
        return reservation;
    }

    @Override
    public void deleteById(Long id) {
        reservations.remove(id);
    }
}
