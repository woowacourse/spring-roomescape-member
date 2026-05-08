package roomescape.user.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.user.dao.ReservationDao;
import roomescape.user.dao.ReservationTimeDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao timeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao timeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.selectAll();
    }

    @Transactional
    public Reservation add(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = timeDao.selectById(timeId);
        Reservation reservation = new Reservation(name, date, time, themeId);

        boolean isAvailable = reservationDao.isAvailable(themeId, date, timeId);

        if (!isAvailable) {
            throw new IllegalArgumentException("[ERROR] 예약할 수 없습니다.");
        }

        return reservationDao.insert(reservation);
    }

    public void delete(Long id) {
        reservationDao.delete(id);
    }
}
