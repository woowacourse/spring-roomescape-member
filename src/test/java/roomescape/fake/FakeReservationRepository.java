package roomescape.fake;

import org.springframework.dao.EmptyResultDataAccessException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.ConflictException;
import roomescape.exception.code.ConflictCode;
import roomescape.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FakeReservationRepository implements ReservationRepository {
    private final List<Reservation> reservations;
    private long id = 1;

    public FakeReservationRepository(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservations.stream()
                .filter(reservation -> id.equals(reservation.id()))
                .findFirst();
    }

    @Override
    public List<Reservation> findAllReservations() {
        return List.copyOf(reservations);
    }

    @Override
    public Reservation addReservation(Reservation reservation) {
        boolean duplicated = reservations.stream()
                .anyMatch(r -> r.date().equals(reservation.date())
                        && r.timeId() == reservation.timeId()
                        && r.themeId() == reservation.themeId());
        if (duplicated) {
            throw new ConflictException(ConflictCode.RESERVATION_DUPLICATED);
        }
        Reservation newReservation = new Reservation(id++, reservation.name(), reservation.date(), reservation.time(), reservation.theme());
        reservations.add(newReservation);
        return newReservation;
    }

    @Override
    public void deleteById(Long id) {
        reservations.removeIf(reservation -> Objects.equals(reservation.id(), id));
    }

    @Override
    public void updateCancelled(Long id) {
        boolean removed = reservations.removeIf(reservation -> Objects.equals(reservation.id(), id));
        if (!removed) {
            throw new EmptyResultDataAccessException(1);
        }
    }

    @Override
    public List<Reservation> findReservationsByName(String name) {
        return reservations.stream()
                .filter(reservation -> reservation.name().equalsIgnoreCase(name))
                .toList();
    }

    @Override
    public int countReservationsOf(LocalDate date, long timeId, long themeId) {
        return (int) reservations.stream()
                .filter(r -> r.date().equals(date))
                .filter(r -> r.timeId() == timeId)
                .filter(r -> r.themeId() == themeId)
                .count();
    }

    @Override
    public void updateReservation(Long id, LocalDate date, long timeId) {
        Reservation reservation = findById(id).orElseThrow(IllegalStateException::new);
        ReservationTime updatedTime = new ReservationTime(timeId, reservation.time().startAt());
        Reservation updated = new Reservation(
                reservation.id(),
                reservation.name(),
                date,
                updatedTime,
                reservation.theme());
        int index = reservations.indexOf(reservation);
        reservations.set(index, updated);
    }
}
