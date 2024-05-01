package roomescape.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservation.request.ReservationRequest;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

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

    public Reservation save(ReservationRequest request) {
        if(reservationDao.existsByDateTime(request.date(), request.timeId())){
            throw new IllegalArgumentException("Reservation already exists");
        }
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId());
        return reservationDao.save(new Reservation(0, request.name(), request.date(), reservationTime));
    }

    public void delete(long id) {
        reservationDao.deleteById(id);
    }
}
