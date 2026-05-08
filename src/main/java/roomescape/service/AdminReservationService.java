package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.dao.ReservationDao;

@Service
public class AdminReservationService {

    private final ReservationDao reservationDao;

    public AdminReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<Reservation> getAllReservations() {
        return reservationDao.findAll();
    }
}