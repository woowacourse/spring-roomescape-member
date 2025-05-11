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
    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    @Override
    public void save(Reservation reservation) {
        long savedId = reservationDao.save(reservation);
        reservation.setId(savedId);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        reservationDao.delete(id);
    }

    @Override
    public boolean hasAnotherReservation(ReservationDate date, Long timeId) {
        return reservationDao.existReservationOf(date, timeId);
    }

    @Override
    public Reservation findById(Long id) {
        return reservationDao.findById(id).
            orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 ID 입니다."));
    }

    @Override
    public List<Reservation> findOf(String startDate, String endDate, Long memberId, Long themeId) {
        return reservationDao.findOf(startDate, endDate, memberId, themeId);
    }
}
