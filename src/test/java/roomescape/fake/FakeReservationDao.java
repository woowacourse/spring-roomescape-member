package roomescape.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.Theme;
import roomescape.persistence.dao.ReservationDao;
import roomescape.presentation.dto.ReservationAvailableTimeResponse;

public class FakeReservationDao implements ReservationDao {

    private final List<Reservation> reservations;
    private final List<PlayTime> times;
    private final AtomicLong reservationAtomicLong = new AtomicLong(1L);

    private int index = 1;

    public FakeReservationDao() {
        this.reservations = new ArrayList<>();
        this.times = new ArrayList<>();
    }

    @Override
    public Long save(final Reservation reservation) {
        final Long id = reservationAtomicLong.getAndIncrement();
        final Reservation insertReservation = new Reservation(id, reservation.getName(), reservation.getDate(),
                reservation.getPlayTime(), reservation.getTheme());
        reservations.add(insertReservation);
        return id;
    }

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean remove(final Long id) {
        int beforeSize = reservations.size();
        reservations.removeIf(reservation -> reservation.getId().equals(id));
        int afterSize = reservations.size();
        int deletedCount = beforeSize - afterSize;
        return deletedCount >= 1;

    }

    @Override
    public boolean existsByDateAndTimeAndTheme(
            final LocalDate date,
            final PlayTime time,
            final Theme theme
    ) {
        final Long timeId = time.getId();
        final Long themeId = theme.getId();

        return reservations.stream()
                .anyMatch(reservation ->
                        reservation.getDate().equals(date) &&
                                reservation.getPlayTime().getId().equals(timeId) &&
                                reservation.getTheme().getId().equals(themeId)
                );
    }

    @Override
    public List<ReservationAvailableTimeResponse> findAvailableTimesByDateAndTheme(final LocalDate date,
                                                                                   final Theme theme) {
        return List.of();
    }
}
