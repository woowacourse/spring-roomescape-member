package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.reservation.model.Reservation;

public class FakeReservationRepository implements ReservationRepository {
    private final List<Reservation> reservations = new ArrayList<>();

    @Override
    public Reservation save(final Reservation reservation) {
        Reservation newReservation = Reservation.of((long) reservations.size() + 1, reservation);
        reservations.add(newReservation);
        return newReservation;
    }

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        int index = id.intValue() - 1;
        if (reservations.size() > index) {
            return Optional.of(reservations.get(index));
        }
        return Optional.empty();
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(final LocalDate date, final Long themeId) {
        return reservations.stream()
                .filter(reservation -> reservation.isSameDate(date)
                        && reservation.isSameTheme(themeId))
                .toList();
    }

    @Override
    public boolean existsByDateAndTimeAndTheme(final LocalDate date, final Long timeId, final Long themeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isSameDate(date)
                        && reservation.isSameTimeId(timeId)
                        && reservation.isSameTheme(themeId));
    }

    @Override
    public boolean existsById(final Long id) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isSameId(id));
    }

    @Override
    public boolean existsByTimeId(final Long id) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isSameTimeId(id));
    }

    @Override
    public boolean existsByThemeId(final Long id) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isSameTheme(id));
    }

    @Override
    public void deleteById(final Long id) {
        reservations.remove(id.intValue() - 1);
    }
}
