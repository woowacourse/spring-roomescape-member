package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationRepository;

public class FakeReservationDao implements ReservationRepository {
    private final Map<Long, Reservation> reservations = new HashMap<>();
    private final Map<Long, Long> reservationList = new HashMap<>();

    @Override
    public Reservation save(final Reservation reservation) {
        reservations.put((long) reservations.size() + 1, reservation);
        return new Reservation(
                (long) reservations.size(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public List<Reservation> findAll() {
        return reservations.values()
                .stream()
                .toList();
    }

    @Override
    public boolean deleteById(final long reservationId) {
        if (!reservations.containsKey(reservationId)) {
            return false;
        }
        reservations.remove(reservationId);
        return true;
    }

    @Override
    public List<Reservation> findAllByTimeId(final long timeId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getTime().getId() == timeId)
                .toList();
    }

    @Override
    public boolean existsByDateTime(final LocalDate date, final long timeId) {
        List<Reservation> reservationList = reservations.values().stream()
                .filter(reservation -> reservation.getTime().getId() == timeId && reservation.getDate().equals(date))
                .toList();
        return !reservationList.isEmpty();
    }

    @Override
    public Optional<Reservation> findBy(final LocalDate date, final long timeId, final long themeId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getDate().equals(date) &&
                        reservation.getTime().getId() == timeId &&
                        reservation.getTheme().getId() == themeId)
                .findFirst();
    }

    @Override
    public void saveReservationList(final long memberId, final long reservationId) {
        reservationList.put(memberId, reservationId);
    }
}
