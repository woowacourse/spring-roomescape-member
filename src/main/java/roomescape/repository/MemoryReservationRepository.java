package roomescape.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.model.Reservation;

public class MemoryReservationRepository implements ReservationRepository {
    private final List<Reservation> reservations;
    private final AtomicLong id = new AtomicLong(1L);

    public MemoryReservationRepository() {
        this.reservations = new ArrayList<Reservation>();
    }

    @Override
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }

    @Override
    public Reservation addReservation(Reservation reservation) {
//        Reservation reservation = reservationRequestDto.toEntity(id.getAndIncrement(), reservationTime, theme);
        reservations.add(reservation);
        return reservation;
    }

    @Override
    public void deleteReservation(Long id) {
        for (Reservation reservation : reservations) {
            if (reservation.getId().equals(id)) {
                reservations.remove(reservation);
                return;
            }
        }
    }

    @Override
    public boolean contains(LocalDate reservationDate, Long timeId) {
        return false;
    }
}
