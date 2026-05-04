package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import java.time.LocalDate;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<Reservation> findAll(){
        return reservationDao.findAll();
    }

    public Reservation save(String name, LocalDate date, Long timeId) {
        ReservationTime time = reservationTimeDao.findById(timeId);
        Reservation reservation = new Reservation(name, date, time);
        return reservationDao.save(reservation);
    }

    public void delete(Long id){
        validateHasReservation(id);
        reservationDao.deleteById(id);
    }

    private void validateHasReservation(Long id) {
        boolean hasReservation = reservationDao.existByTimeId(id);
        if(!hasReservation){
            throw new IllegalArgumentException("존재하지 않는 예약니다.");
        }
    }
}
