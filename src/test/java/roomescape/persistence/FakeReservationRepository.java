package roomescape.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.business.Reservation;

public final class FakeReservationRepository implements ReservationRepository {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getId(), id))
                .findFirst();
    }

    @Override
    public List<Reservation> findAllByThemeAndMemberAndDate(Long themeId,
                                                            Long memberId,
                                                            LocalDate dateFrom,
                                                            LocalDate dateTo) {
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getTheme().getId(), themeId)
                        && Objects.equals(reservation.getMember().getId(), memberId)
                        && reservation.getDate().isAfter(dateFrom) && reservation.getDate().isBefore(dateTo))
                .toList();
    }

    @Override
    public Long add(Reservation reservation) {
        Reservation savedReservation = new Reservation(
                idGenerator.getAndIncrement(),
                reservation.getMember(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
        reservations.add(savedReservation);
        return savedReservation.getId();
    }

    @Override
    public void deleteById(Long id) {
        reservations.removeIf(reservation -> Objects.equals(reservation.getId(), id));
    }

    @Override
    public boolean existsByReservation(Reservation otherReservation) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isSameReservation(otherReservation));
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTime().getId().equals(timeId));
    }

    @Override
    public boolean existByThemeId(Long id) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTheme().getId().equals(id));
    }
}
