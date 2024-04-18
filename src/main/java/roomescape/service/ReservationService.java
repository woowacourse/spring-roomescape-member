package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationAddRequest;
import roomescape.repository.ReservationDao;

@Service
public class ReservationService {

    private ReservationDao reservationDao;

    ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<Reservation> findAllReservation() {
        return reservationDao.findAll();
    }

    public Reservation addReservation(ReservationAddRequest reservationAddRequest) {
        return reservationDao.insert(reservationAddRequest);
    }

    public void removeReservation(Long id) {
        if (reservationDao.findById(id).isEmpty()) {
            throw new IllegalArgumentException("해당 id를 가진 예약이 존재하지 않습니다.");
        }
        reservationDao.deleteById(id);
    }
}
