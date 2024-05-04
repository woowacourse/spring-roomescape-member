package roomescape.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
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
    public List<Reservation> findAll() {
        return reservations.stream()
                .sorted()
                .toList();
    }

    @Override
    public List<Reservation> findByThemeAndDate(Theme theme, LocalDate date) {
        return reservations.stream()
                .filter(reservation -> reservation.isThemeOf(theme.getId()))
                .filter(reservation -> reservation.isDateOf(date))
                .toList();
    }

    @Override
    public void delete(long id) {
        reservations.stream()
                .filter(reservation -> reservation.hasSameId(id))
                .findAny()
                .ifPresent(reservations::remove);
    }
}
