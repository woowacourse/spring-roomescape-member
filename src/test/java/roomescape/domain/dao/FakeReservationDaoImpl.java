package roomescape.domain.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
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
    public boolean existsByTimeId(Long id) {
        return true;
    }

    @Override
    public int findByDateAndTime(ReservationDate date, Long timeId) {
        return 0;
    }

    private Reservation findById(long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .orElseThrow(() -> new InvalidReservationException("존재하지 않는 예약번호 입니다."));
    }

    @Override
    public int findAlreadyExistReservationBy(String date, long timeId, Long themeId) {
        return 0;
    }
}
