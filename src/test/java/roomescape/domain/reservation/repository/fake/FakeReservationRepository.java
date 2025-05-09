package roomescape.domain.reservation.repository.fake;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    private static final Long INITIAL_ID = 1L;

    private final AtomicLong id = new AtomicLong(INITIAL_ID);
    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();

    @Override
    public List<Reservation> findAll() {
        return reservations.values()
                .stream()
                .toList();
    }

    @Override
    public List<Reservation> findReservations(final Long themeId, final Long userId, final LocalDate dateFrom, final LocalDate dateTo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Reservation> findByDateAndThemeId(final LocalDate date, final Long themeId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        if (reservations.containsKey(id)) {
            return Optional.ofNullable(reservations.get(id));
        }

        return Optional.empty();
    }

    @Override
    public Reservation save(final Reservation reservation) {
        if (reservation.existId() && !reservations.containsKey(reservation.getId())) {
            throw new EntityNotFoundException("Reservation with id " + reservation.getId() + " not found");
        }

        if (reservation.existId()) {
            reservations.put(reservation.getId(), reservation);
            return reservation;
        }

        final Reservation reservationWithId = new Reservation(id.getAndIncrement(), reservation.getUser(),
                reservation.getReservationDate(), reservation.getReservationTime(), reservation.getTheme());

        reservations.put(reservationWithId.getId(), reservationWithId);
        return reservationWithId;
    }

    @Override
    public void deleteById(final Long id) {
        if (!reservations.containsKey(id)) {
            throw new EntityNotFoundException("Reservation with id " + id + " not found");
        }

        reservations.remove(id);
    }

    @Override
    public boolean existsByDateAndTimeId(final LocalDate date, final Long timeId) {
        return reservations.values()
                .stream()
                .anyMatch(reservation -> sameReservationDateTime(reservation, date, timeId));
    }

    @Override
    public boolean existsByTimeId(final Long timeId) {
        return reservations.values()
                .stream()
                .anyMatch(reservation -> reservation.getReservationTimeId()
                        .equals(timeId));
    }

    @Override
    public boolean existsByThemeId(final Long themeId) {
        return reservations.values()
                .stream()
                .anyMatch(reservation -> reservation.getThemeId()
                        .equals(themeId));

    }

    private boolean sameReservationDateTime(final Reservation reservation, final LocalDate date, final Long timeId) {
        return reservation.getReservationDate()
                .equals(date) && reservation.getReservationTime()
                .getId()
                .equals(timeId);
    }

    public void deleteAll() {
        reservations.clear();
        id.set(INITIAL_ID);
    }
}
