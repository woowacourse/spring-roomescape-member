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
    public List<Reservation> findReservationsByName(String name) {
        return reservations.stream()
            .filter(reservation -> reservation.getName().equals(name))
            .toList();
    }

    @Override
    public Optional<Reservation> findReservationById(Long id) {
        return reservations.stream()
            .filter(reservation -> reservation.getId().equals(id))
            .findFirst();
    }

    @Override
    public Optional<Reservation> findReservationByDateTimeAndThemeId(LocalDate date, Long timeId,
        Long themeId) {
        return reservations.stream()
            .filter(reservation -> Objects.equals(reservation.getDate(), date))
            .filter(reservation -> Objects.equals(reservation.getTime().getId(), timeId))
            .filter(reservation -> Objects.equals(reservation.getTheme().getId(), themeId))
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
    public void updateReservationById(Long id, LocalDate date, Long timeId) {
        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);

            if (!reservation.getId().equals(id)) {
                continue;
            }

            Time time = reservation.getTime();
            Reservation updatedReservation = Reservation.reconstruct(
                reservation.getId(),
                reservation.getName(),
                date,
                Time.reconstruct(timeId, time.getStartAt()),
                reservation.getTheme()
            );
            reservations.set(i, updatedReservation);
            return;
        }
    }

    @Override
    public int deleteReservationById(Long id) {
        int beforeSize = reservations.size();
        reservations.removeIf(reservation -> Objects.equals(reservation.getId(), id));

        return beforeSize - reservations.size();
    }
}
