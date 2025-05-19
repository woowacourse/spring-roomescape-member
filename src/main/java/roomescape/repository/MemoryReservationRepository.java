package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.model.Reservation;

public class MemoryReservationRepository implements ReservationRepository {

    @Override
    public List<Reservation> getReservations(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        return List.of();
    }

    @Override
    public Reservation addReservation(Reservation reservation) {
        return null;
    }

    @Override
    public void deleteReservation(Long id) {

    }
}
