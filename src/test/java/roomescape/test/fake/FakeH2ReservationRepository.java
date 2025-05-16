package roomescape.test.fake;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationRepository;

public class FakeH2ReservationRepository implements ReservationRepository {

    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public List<Reservation> findAll() {
        return reservations.values().stream().toList();
    }

    @Override
    public Optional<Reservation> findById(long id) {
        if (!reservations.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(reservations.get(id));
    }

    @Override
    public boolean checkAlreadyReserved(LocalDate date, long timeId, long themeId) {
        return reservations.values()
                .stream()
                .anyMatch(reservation ->
                        reservation.getDate().equals(date)
                                && reservation.getTime().getId().equals(timeId)
                                && reservation.getTheme().getId().equals(themeId));
    }

    @Override
    public boolean checkExistenceInTime(long reservationTimeId) {
        return reservations.values().stream()
                .anyMatch(reservation -> reservation.getTime().getId().equals(reservationTimeId));
    }

    @Override
    public boolean checkExistenceInTheme(final long themeId) {
        return reservations.values().stream()
                .anyMatch(reservation -> reservation.getTheme().getId().equals(themeId));
    }

    @Override
    public long add(Reservation reservation) {
        Reservation newReservation = new Reservation(
                index.getAndIncrement(), reservation.getMember(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
        reservations.put(newReservation.getId(), newReservation);
        return newReservation.getId();
    }

    @Override
    public void deleteById(long id) {
        reservations.remove(id);
    }

    @Override
    public List<Reservation> saerch(final Long themeId, final Long memberId, final LocalDate dateFrom,
                                    final LocalDate dateTo) {
        return List.of();
    }
}
