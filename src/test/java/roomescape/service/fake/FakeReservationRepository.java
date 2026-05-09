package roomescape.service.fake;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    private final List<Reservation> reservations = new CopyOnWriteArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    @Override
    public Reservation save(Reservation reservation) {
        Reservation saved = new Reservation(
                counter.getAndIncrement(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTheme(),
                reservation.getTime()
        );
        reservations.add(saved);
        return saved;
    }

    @Override
    public void delete(long id) {
        reservations.removeIf(reservation -> reservation.getId().equals(id));
    }

    @Override
    public boolean existByDateAndThemeIdAndTimeId(LocalDate date, long themeId, long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getDate().equals(date) &&
                        reservation.getTheme().getId().equals(themeId) &&
                        reservation.getTime().getId().equals(timeId));
    }

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }
}
