package roomescape.service.support;

import roomescape.domain.Reservation;
import roomescape.repository.ReservationRepository;
import roomescape.repository.dto.ReservationTimesWithStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FakeReservationRepository implements ReservationRepository {

    private final List<Reservation> reservations = new ArrayList<>();
    private Reservation savedReservation;
    private boolean deleteResult = true;

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }

    @Override
    public Reservation save(final Reservation newReservation) {
        savedReservation = newReservation;
        Reservation savedReservationWithId = Reservation.of(
                1L,
                newReservation.getCustomerName(),
                newReservation.getDate(),
                newReservation.getTime(),
                newReservation.getTheme()
        );
        reservations.add(savedReservationWithId);
        return savedReservationWithId;
    }

    @Override
    public boolean deleteById(final Long reservationId) {
        return deleteResult;
    }

    @Override
    public List<ReservationTimesWithStatus> findReservationTimeStatusesByDateAndThemeId(final LocalDate date, final Long themeId) {
        return List.of();
    }

    public Reservation savedReservation() {
        return savedReservation;
    }

    public void failToDelete() {
        deleteResult = false;
    }
}
