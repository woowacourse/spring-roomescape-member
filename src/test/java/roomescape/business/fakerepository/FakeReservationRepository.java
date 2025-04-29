package roomescape.business.fakerepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import roomescape.business.Reservation;
import roomescape.persistence.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    private final List<Reservation> reservations = new ArrayList<>();

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }

    @Override
    public Reservation findById(Long id) {
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getId(), id))
                .findFirst()
                .get();
    }

    @Override
    public Long add(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(1L);
        }
        reservations.add(reservation);
        return reservation.getId();
    }

    @Override
    public void delete(Long id) {
        Reservation reservation = findById(id);
        reservations.remove(reservation);
    }

    @Override
    public boolean existsByDateTime(LocalDate date, LocalTime time) {
        return reservations.stream()
                .anyMatch(reservation -> LocalDateTime.of(reservation.getDate(), reservation.getTime().getStartAt())
                        .equals(LocalDateTime.of(date, time)));
    }
}
