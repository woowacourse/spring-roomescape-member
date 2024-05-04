package roomescape.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
import roomescape.domain.Reservations;
import roomescape.domain.Theme;

public class CollectionReservationRepository implements ReservationRepository {
    private final List<Reservation> reservations;
    private final AtomicLong atomicLong;


    public CollectionReservationRepository() {
        this.reservations = new ArrayList<>();
        this.atomicLong = new AtomicLong(0);
    }

    @Override
    public Reservation save(Reservation reservation) {
        Reservation saved = new Reservation(atomicLong.incrementAndGet(), reservation);
        reservations.add(saved);
        return saved;
    }

    @Override
    public Reservations findAll() {
        List<Reservation> findReservations = this.reservations.stream()
                .sorted()
                .toList();
        return new Reservations(findReservations);
    }

    @Override
    public Reservations findByThemeAndDate(Theme theme, LocalDate date) {
        List<Reservation> findReservations = reservations.stream()
                .filter(reservation -> reservation.isThemeOf(theme.getId()))
                .filter(reservation -> reservation.isDateOf(date))
                .toList();
        return new Reservations(findReservations);
    }

    @Override
    public boolean existByTimeId(long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isReservationTimeOf(timeId));
    }

    @Override
    public boolean existByThemeId(long themeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isThemeOf(themeId));
    }

    @Override
    public void delete(long id) {
        reservations.stream()
                .filter(reservation -> reservation.hasSameId(id))
                .findAny()
                .ifPresent(reservations::remove);
    }
}
