package roomescape.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
import roomescape.exception.InvalidReservationException;

public class FakeReservationDaoImpl implements ReservationDao {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public List<Reservation> findAllReservation() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public void saveReservation(Reservation reservation) {
        reservation.setId(index.getAndIncrement());
        reservations.add(reservation);
    }

    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = findById(id);
        reservations.remove(reservation);
    }

    @Override
    public Boolean existsByTimeId(Long id) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTimeId().equals(id));
    }

    @Override
    public Boolean existsByThemeId(final Long id) {
        return true;
    }

    private Reservation findById(long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .orElseThrow(() -> new InvalidReservationException("존재하지 않는 예약번호 입니다."));
    }

    @Override
    public Boolean existsReservationBy(LocalDate date, Long timeId, Long themeId) {
        return reservations.stream()
                .anyMatch(reservation ->
                        reservation.getDate().equals(date)
                                && reservation.getTimeId().equals(timeId)
                                && reservation.getThemeId().equals(themeId));
    }
}
