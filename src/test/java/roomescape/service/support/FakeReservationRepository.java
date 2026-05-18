package roomescape.service.support;

import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.exception.ReservationNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.dto.ReservationTimesWithStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeReservationRepository implements ReservationRepository  {

    private final List<Reservation> reservations = new ArrayList<>();
    private Reservation savedReservation;

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }

    @Override
    public Optional<Reservation> findById(final Long reservationId) {
        return reservations.stream()
                .filter(reservation -> reservation.getId().equals(reservationId))
                .findFirst();
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
    public Reservation update(final Reservation updatedReservation) {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getId().equals(updatedReservation.getId())) {
                reservations.set(i, updatedReservation);
                return updatedReservation;
            }
        }

        throw new ReservationNotFoundException();
    }

    @Override
    public boolean deleteById(final Long reservationId) {
        return reservations.removeIf(reservation -> reservation.getId().equals(reservationId));
    }

    @Override
    public List<ReservationTimesWithStatus> findReservationTimeStatusesByDateAndThemeId(final LocalDate date, final Long themeId) {
        return List.of();
    }

    @Override
    public List<Reservation> findAllByCustomerName(final Name customerName) {
        return reservations.stream()
                .filter(reservation -> reservation.getCustomerName().equals(customerName.getName()))
                .toList();
    }

    public Reservation savedReservation() {
        return savedReservation;
    }

    public void add(final Reservation reservation) {
        reservations.add(reservation);
    }
}
