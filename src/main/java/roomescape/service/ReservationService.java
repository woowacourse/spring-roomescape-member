package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.dto.CreateReservationRequest;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<Reservation> getReservations() {
        return reservationDao.findAll();
    }

    public Reservation createReservation(CreateReservationRequest request) {
        Long newReservationId = reservationDao.save(request);
        return reservationDao.findById(newReservationId);
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
