package roomescape.reservation.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservation.Reservation;

public class FakeReservationDao implements ReservationDao {

    private final List<Reservation> fakeReservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    public FakeReservationDao(Reservation... reservations) {
        Arrays.stream(reservations).forEach(reservation -> fakeReservations.add(reservation));
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(fakeReservations);
    }

    @Override
    public Long create(Reservation reservation) {
        Reservation reservationWithId = new Reservation(index.getAndIncrement(), reservation.getName(),
                reservation.getDate(), reservation.getReservationTime());
        fakeReservations.add(reservationWithId);
        return reservationWithId.getId();
    }

    @Override
    public Integer delete(Long id) {
        fakeReservations.removeIf(reservation -> reservation.getId().equals(id));
        return fakeReservations.size();
    }
}
