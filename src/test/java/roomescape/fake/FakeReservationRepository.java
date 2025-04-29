package roomescape.fake;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeReservationRepository implements ReservationRepository {

    private Long id = 0L;
    private List<Reservation> reservations = new ArrayList<>();

    public FakeReservationRepository(Reservation... reservations) {
        this((long) reservations.length, new ArrayList<>(List.of(reservations)));
    }

    private FakeReservationRepository(Long id, List<Reservation> reservations) {
        this.id = id;
        this.reservations = reservations;
    }

    @Override
    public List<Reservation> findAll() {
        return List.copyOf(reservations);
    }

    @Override
    public Long create(Reservation reservation) {
        Reservation newReservation = new Reservation(++id, reservation.getName(), reservation.getDate(),
                reservation.getTime());
        reservations.add(newReservation);
        return id;
    }

    @Override
    public void deleteById(Long reservationId) {
        reservations = reservations.stream()
                .filter(reservation -> reservation.getId() != reservationId)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Reservation> findById(Long reservationId) {
        return reservations.stream()
                .filter(reservation -> reservation.getId() == reservationId)
                .findFirst();
    }

    @Override
    public boolean existByTimeId(final Long reservationTimeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTime().id() == reservationTimeId);
    }
}
