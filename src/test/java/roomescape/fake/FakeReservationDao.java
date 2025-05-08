package roomescape.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import roomescape.business.domain.reservation.Reservation;
import roomescape.repository.ReservationRepository;

public class FakeReservationDao implements ReservationRepository {

    List<Reservation> reservations = new ArrayList<>();
    Long index = 1L;

    @Override
    public Reservation save(final Reservation reservation) {
        Reservation newReservation = new Reservation(index++, reservation.getName(), reservation.getDate(),
                reservation.getTime(), reservation.getTheme());
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
    public boolean existsByTimeId(final long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTimeId() == timeId);
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTheme().getId() == themeId);
    }

    @Override
    public boolean existsByDateAndTimeAndTheme(final LocalDate date, final long timeId, final long themeId) {
        return reservations.stream()
                .anyMatch(reservation ->
                        reservation.getTheme().getId() == themeId
                                && reservation.getDate().isEqual(date)
                                && reservation.getTime().getId() == timeId);
    }

    public Reservation findById(final long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .orElseThrow();
    }
}
