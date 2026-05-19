package roomescape.fake;

import roomescape.domain.Reservation;
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
    public int relocateToCanceledReservation(Long id) {
        return 0;
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
    public void updateReservation(Reservation reservation) {
        Reservation existing = findById(reservation.id()).orElseThrow(IllegalStateException::new);
        int index = reservations.indexOf(existing);
        reservations.set(index, reservation);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservations.stream()
                .filter(reservation -> id.equals(reservation.id()))
                .findFirst();
    }
}
