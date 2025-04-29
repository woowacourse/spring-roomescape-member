package roomescape.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import roomescape.repository.ReservationDao;
import roomescape.service.reservation.Reservation;

public class FakeReservationDao implements ReservationDao {

    List<Reservation> reservations = new ArrayList<>();
    Long index = 1L;

    @Override
    public Reservation save(final Reservation reservation) {
        Reservation newReservation = new Reservation(index++, reservation.getName(), reservation.getDate(),
                reservation.getTime());
        reservations.add(newReservation);
        return newReservation;
    }

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }

    @Override
    public void deleteById(final long id) {
        Reservation reservation = findById(id);
        reservations.remove(reservation);
    }

    @Override
    public boolean isExistsByDateAndTimeId(final LocalDate date, final long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getDate().equals(date) && reservation.getTimeId() == timeId);
    }

    @Override
    public boolean isExistsByTimeId(final long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTimeId() == timeId);
    }

    public Reservation findById(final long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .orElseThrow();
    }
}
