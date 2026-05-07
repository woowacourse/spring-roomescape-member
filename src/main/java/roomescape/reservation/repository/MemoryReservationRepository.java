package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservation.domain.Reservation;

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
    public boolean existsByDateAndThemeIdAndTimeId(LocalDate date, long themeId, long timeId) {
        for (Reservation reservation : reservations) {
            if (reservation.getDate().equals(date)
                    && reservation.getTheme().getId() == themeId
                    && reservation.getTime().getId() == timeId) {
                return true;
            }
        }

        return false;
    }

}
