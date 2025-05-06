package roomescape.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;

public class FakeReservationDaoImpl implements ReservationDao {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public long save(Reservation reservation) {
        reservations.add(reservation);
        return index.getAndIncrement();
    }

    @Override
    public void delete(Long id) {
        Reservation reservation = findById(id).orElseThrow(() -> new IllegalArgumentException());
        reservations.remove(reservation);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservations.stream()
            .filter(reservation -> reservation.getId().equals(id))
            .findFirst();
    }

    @Override
    public int countExistReservationByTime(Long id) {
        return (int) reservations.stream()
            .filter(reservation -> reservation.getId().equals(id))
            .count();
    }

    @Override
    public int countExistReservationByTheme(Long id) {
        return (int) reservations.stream()
            .filter(reservation -> reservation.getId().equals(id))
            .count();
    }

    @Override
    public int countAlreadyReservationOf(ReservationDate date, Long timeId) {
        return (int) reservations.stream()
            .filter(reservation -> reservation.getReservationDate().equals(date))
            .filter(reservation -> reservation.getTimeId() == timeId)
            .count();
    }
}
