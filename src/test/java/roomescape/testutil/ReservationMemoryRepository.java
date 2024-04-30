package roomescape.testutil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.model.Reservation;
import roomescape.repository.ReservationRepository;

public class ReservationMemoryRepository implements ReservationRepository {

    private final AtomicLong reservationId = new AtomicLong(1);
    private final List<Reservation> reservations = new ArrayList<>();

    @Override
    public Reservation save(final Reservation reservation) {
        final Long savedReservationId = reservationId.getAndIncrement();
        final Reservation savedReservation = new Reservation(savedReservationId, reservation.getName().getValue(), reservation.getDate(), reservation.getTime());
        reservations.add(savedReservation);
        return savedReservation;
    }

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public void deleteById(final Long id) {
        reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst()
                .ifPresent(reservations::remove);
    }

    @Override
    public boolean existByTimeId(final Long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTimeId().equals(timeId));
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findAny();
    }

    @Override
    public boolean existByDateAndTimeId(final LocalDate date, final Long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTimeId().equals(timeId) && reservation.getDate().equals(date));
    }
}
