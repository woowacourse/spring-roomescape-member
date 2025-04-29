package roomescape.reservation.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    @Override
    public Optional<Reservation> findByTimeId(Long id) {
        return fakeReservations.stream()
                .filter(reservation -> reservation.getReservationTime().getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return fakeReservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Reservation> findByDateTime(LocalDate date, LocalTime time) {
        return fakeReservations.stream()
                .filter(reservation -> reservation.getDate().equals(date) && reservation.getReservationTime()
                        .getStartAt().equals(time))
                .findFirst();
    }
}
