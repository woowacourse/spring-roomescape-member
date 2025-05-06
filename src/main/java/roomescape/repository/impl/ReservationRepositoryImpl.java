package roomescape.repository.impl;

import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.repository.ReservationRepository;

@Component
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationDao reservationDao;

    public ReservationRepositoryImpl(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Override
    public List<Reservation> findAllReservation() {
        return reservationDao.findAllReservation();
    }

    @Override
    public void saveReservation(Reservation reservation) {
        long savedId = reservationDao.saveReservation(reservation);
        reservation.setId(savedId);
    }

    @Override
    public void deleteReservation(Long id) {
        findById(id);
        reservationDao.deleteReservation(id);
    }

    @Override
    public boolean hasAnotherReservation(ReservationDate date, Long timeId) {
        return reservationDao.countAlreadyReservationOf(date, timeId) != 0;
    }

    @Override
    public Reservation findById(Long id) {
        return reservationDao.findById(id).
            orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 ID 입니다."));
    }
}
