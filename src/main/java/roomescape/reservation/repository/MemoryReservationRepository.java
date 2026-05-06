package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;

public class MemoryReservationRepository implements ReservationRepository {
    List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(0);

    @Override
    public List<Reservation> findAll() {
        return List.copyOf(reservations);
    }

    @Override
    public Optional<Reservation> findById(final long id) {
        return reservations.stream()
                .filter(it -> it.getId() == id)
                .findFirst();
    }

    @Override
    public void deleteById(long id) {
        Reservation reservation = reservations.stream()
                .filter(it -> it.getId() == id)
                .findFirst()
                .orElseThrow(RuntimeException::new);

        reservations.remove(reservation);
    }

    @Override
    public Reservation save(Reservation reservation) {
        Reservation newReservation = reservation.withId(index.incrementAndGet());
        reservations.add(newReservation);
        return newReservation;
    }

    @Override
    public boolean existsByDateAndTimeId(LocalDate date, long timeId) {
        for (Reservation reservation : reservations) {
            if (reservation.getDate().equals(date) && reservation.getTime().getId() == timeId) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<Long> findAllByDateAndThemeId(final LocalDate date, final long themeId) {
        return reservations.stream()
                .filter(reservation -> reservation.getDate().equals(date))
                .filter(reservation -> reservation.getTime().getTheme().getId() == themeId)
                .map(reservation -> reservation.getTime().getId())
                .toList();
    }

    @Override
    public List<Theme> findPopularThemes(final int period, final int limit) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(period);

        return reservations.stream()
                .filter(reservation ->
                        !reservation.getDate().isBefore(start) &&
                                !reservation.getDate().isAfter(end)
                )
                .map(reservation -> reservation.getTime().getTheme())
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Theme, Long>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }

}
