package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservation.domain.Reservation;

public class FakeReservationRepository implements ReservationRepository {
    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public List<Reservation> getAll() {
        return reservations.entrySet().stream()
                .map(entry -> {
                    Reservation value = entry.getValue();
                    return Reservation.of(entry.getKey(), value.getName(),
                            value.getDate(), value.getTime(), value.getTheme());
                })
                .toList();
    }

    @Override
    public Reservation put(final Reservation reservation) {
        Long id = index.getAndIncrement();
        reservations.put(id, reservation);
        return Reservation.of(id, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    @Override
    public boolean deleteById(final Long id) {
        return reservations.remove(id) != null;
    }

    @Override
    public boolean existsByTimeId(final Long id) {
        return reservations.values().stream()
                .anyMatch(reservation -> Objects.equals(reservation.getTime().getId(), id));
    }

    @Override
    public boolean existsByThemeId(final Long id) {
        return reservations.values().stream()
                .anyMatch(reservation -> Objects.equals(reservation.getTheme().getId(), id));
    }

    @Override
    public boolean existsByDateAndTimeId(final LocalDate date, final long timeId) {
        return reservations.values().stream()
                .anyMatch(reservation -> Objects.equals(reservation.getDate(), date) && Objects.equals(
                        reservation.getTime().getId(), timeId));
    }

    @Override
    public List<Reservation> findByDateAndThemeId(final LocalDate date, final Long themeId) {
        return reservations.entrySet().stream()
                .filter(entry -> {
                    Reservation reservation = entry.getValue();
                    return reservation.getDate().equals(date) && Objects.equals(reservation.getTheme().getId(),
                            themeId);
                })
                .map(entry -> {
                    Reservation value = entry.getValue();
                    return Reservation.of(entry.getKey(), value.getName(),
                            value.getDate(), value.getTime(), value.getTheme());
                })
                .toList();
    }
}

