package roomescape.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import roomescape.business.domain.reservation.Reservation;
import roomescape.repository.ReservationRepository;

public class FakeReservationDao implements ReservationRepository {

    List<Reservation> reservations2 = new ArrayList<>();
    Long index = 1L;

    @Override
    public Reservation save(final Reservation reservation) {
        Reservation newReservation = reservation.withId(index++);
        reservations2.add(newReservation);
        return newReservation;
    }

    @Override
    public List<Reservation> findAll() {
        return reservations2;
    }

    @Override
    public void deleteById(final long id) {
        Reservation reservation = findById(id);
        reservations2.remove(reservation);
    }

    @Override
    public boolean existsByTimeId(final long timeId) {
        return reservations2.stream()
                .anyMatch(reservation -> reservation.getDateTime().getTimeId() == timeId);
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        return reservations2.stream()
                .anyMatch(reservation -> reservation.getTheme().getId() == themeId);
    }

    @Override
    public boolean existsByDateAndTimeAndTheme(final LocalDate date, final long timeId, final long themeId) {
        return reservations2.stream()
                .anyMatch(reservation ->
                        reservation.getTheme().getId() == themeId
                                && reservation.getDateTime().getDate().isEqual(date)
                                && reservation.getDateTime().getTimeId() == timeId);
    }

    public Reservation findById(final long id) {
        return reservations2.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .orElseThrow();
    }
}
