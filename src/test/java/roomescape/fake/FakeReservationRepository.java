package roomescape.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    private Long id = 0L;
    private List<Reservation> reservations = new ArrayList<>();

    @Override
    public List<Reservation> findAll() {
        return List.copyOf(reservations);
    }

    @Override
    public Long create(Reservation reservation) {
        Reservation newReservation = new Reservation(++id, reservation.getName(), reservation.getDate(),
                reservation.getTime(), reservation.getTheme());
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

    @Override
    public boolean existByDateAndTimeId(final LocalDate reservationDate, final Long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getDate().equals(reservationDate)
                        && reservation.getTime().id() == timeId);
    }

    @Override
    public boolean existByThemeId(final Long themeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTheme().getId() == themeId);
    }

    @Override
    public List<Reservation> findByThemeIdAndReservationDate(final Long themeId, final LocalDate reservationDate) {
        return reservations.stream()
                .filter(reservation -> reservation.getTheme().getId() == themeId)
                .filter(reservation -> reservation.getDate().equals(reservationDate))
                .toList();
    }
}
