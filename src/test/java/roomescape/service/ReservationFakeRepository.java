package roomescape.service;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationFakeRepository implements ReservationRepository {

    List<Reservation> reservations = new ArrayList<>(List.of(
            new Reservation(
                    1L,
                    "al",
                    LocalDate.of(2024, 1, 20),
                    new ReservationTime(1L, null)
            ),
            new Reservation(
                    2L,
                    "be",
                    LocalDate.of(2024, 2, 19),
                    new ReservationTime(2L, null)
            )
    ));
    AtomicLong index = new AtomicLong(3);

    @Override
    public List<Reservation> findAll() {
        return List.copyOf(reservations);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findAny();
    }

    @Override
    public Reservation save(Reservation reservation) {
        Reservation assignedReservation = reservation.assignId(index.getAndIncrement());
        reservations.add(assignedReservation);
        return assignedReservation;
    }

    @Override
    public int deleteById(Long id) {
        if (reservations.removeIf(reservation -> reservation.getId().equals(id))) {
            return 1;
        }
        return 0;
    }
}
