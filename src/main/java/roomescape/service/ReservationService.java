package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao timeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao timeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.read();
    }

    @Transactional
    public Reservation create(String name, LocalDate date, Long timeId) {
        ReservationTime time = timeDao.findById(timeId);
        Reservation reservation = new Reservation(name, date, time);

        return reservationDao.create(reservation);
    }

    public void delete(Long id) {
        reservationDao.delete(id);
    }
}
