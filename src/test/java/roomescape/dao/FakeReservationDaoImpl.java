package roomescape.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.reservation.dao.ReservationDao;
import roomescape.domain.reservation.model.Reservation;
import roomescape.domain.reservation.model.ReservationDate;

public class FakeReservationDaoImpl implements ReservationDao {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public long save(Reservation reservation) {
        reservations.add(reservation);
        return index.getAndIncrement();
    }

    @Override
    public boolean delete(Long id) {
        Reservation reservation = findById(id)
            .orElseThrow(() -> new IllegalArgumentException());
        return reservations.remove(reservation);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservations.stream()
            .filter(reservation -> reservation.getId().equals(id))
            .findFirst();
    }

    @Override
    public boolean existReservationByTime(Long id) {
        return reservations.stream()
            .anyMatch(reservation -> reservation.getId().equals(id));
    }

    @Override
    public boolean existReservationByTheme(Long id) {
        return reservations.stream()
            .anyMatch(reservation -> reservation.getId().equals(id));
    }

    @Override
    public boolean existReservationOf(ReservationDate date, Long themeId, Long timeId) {
        return reservations.stream()
            .anyMatch(reservation -> reservation.getReservationDate().equals(date)
                && reservation.getThemeId().equals(themeId)
                && reservation.getTimeId() == timeId);
    }

    @Override
    public List<Reservation> findOf(String dateFrom, String dateTo, Long memberId, Long themeId) {
        return List.of();
    }
}
