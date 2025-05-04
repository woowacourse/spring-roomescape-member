package roomescape.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;

public class FakeReservationDaoImpl implements ReservationDao {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public List<Reservation> findAllReservation() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public long saveReservation(Reservation reservation) {
        reservation.setId(index.getAndIncrement());
        reservations.add(reservation);
        return index.longValue();
    }

    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = findById(id).orElseThrow(() -> new IllegalArgumentException());
        reservations.remove(reservation);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservations.stream()
            .filter(reservation -> reservation.getId().equals(id))
            .findFirst();
    }

    @Override
    public int findByTimeId(Long id) {
        return 0;
    }

    @Override
    public int findByDateAndTime(ReservationDate date, Long timeId) {
        return 0;
    }

    private Reservation findById(long id) {
        return reservations.stream()
            .filter(reservation -> reservation.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약번호 입니다."));
    }

    @Override
    public int findAlreadyExistReservationBy(String date, long timeId, Long themeId) {
        return 0;
    }
}
