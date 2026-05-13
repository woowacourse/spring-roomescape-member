package roomescape.domain.reservation.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.time.entity.Time;

public class FakeReservationRepository implements ReservationRepository {

    private final AtomicLong id = new AtomicLong(0);

    private final List<Reservation> reservations = new ArrayList<>();

    @Override
    public List<Reservation> findAllReservations() {
        return new ArrayList<>(reservations);
    }

    @Override
    public Optional<Reservation> findReservationByDateTimeAndThemeId(LocalDate date, Long timeId,
        Long themeId) {
        return reservations.stream()
            .filter(reservation -> reservation.getDate().equals(date))
            .filter(reservation -> reservation.getTime().getId().equals(timeId))
            .filter(reservation -> reservation.getTheme().getId().equals(themeId))
            .findFirst();
    }

    @Override
    public List<Long> findTimeIdsByDateAndThemeId(LocalDate localDate, Long themeId) {
        return reservations.stream()
            .filter(reservation -> reservation.getDate().equals(localDate))
            .filter(reservation -> reservation.getTheme().getId().equals(themeId))
            .map(Reservation::getTime)
            .map(Time::getId)
            .toList();
    }

    @Override
    public Reservation save(Reservation reservation) {
        Reservation savedReservation = Reservation.reconstruct(id.addAndGet(1), reservation.getName(),
            reservation.getDate(),
            reservation.getTime(),
            reservation.getTheme());
        reservations.add(savedReservation);
        return savedReservation;
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        return reservations.stream()
            .anyMatch(reservation -> reservation.getTime().getId().equals(timeId));
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        return reservations.stream()
            .anyMatch(reservation -> reservation.getTheme().getId().equals(themeId));
    }

    @Override
    public int deleteReservationById(Long id) {
        int beforeSize = reservations.size();
        reservations.removeIf(reservation -> Objects.equals(reservation.getId(), id));

        return beforeSize - reservations.size();
    }
}
