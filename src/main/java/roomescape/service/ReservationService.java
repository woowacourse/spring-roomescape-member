package roomescape.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    public Reservation create(String name, String date, Long timeId) {
        validateId(timeId);
        ReservationTime time = findReservationTime(timeId);
        Reservation reservation = new Reservation(null, name, date, time);
        Long id = reservationDao.insert(reservation);
        return reservationDao.findBy(id);
    }

    public void delete(Long id) {
        validateId(id);
        reservationDao.delete(id);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("[ERROR] id가 올바르지 않습니다.");
        }
    }

    private ReservationTime findReservationTime(Long timeId) {
        try {
            return reservationTimeDao.findBy(timeId);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 예약 시간입니다.");
        }
    }
}
