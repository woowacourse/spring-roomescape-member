package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.dto.reservation.CreateReservationRequest;
import roomescape.repository.ReservationDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<Reservation> getReservations() {
        return reservationDao.findAll();
    }

    @Transactional
    public Reservation createReservation(CreateReservationRequest request) {
        Long newReservationId = reservationDao.save(request);
        return reservationDao.findById(newReservationId);
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
