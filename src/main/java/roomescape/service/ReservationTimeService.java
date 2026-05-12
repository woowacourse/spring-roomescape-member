package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.CreateReservationTimeRequest;
import roomescape.exception.ReservationTimeInUseException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationTimeService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTime> getReservationTimes() {
        return reservationTimeDao.findAll();
    }

    public ReservationTime createReservationTime(CreateReservationTimeRequest request) {
        Long newReservationTimeId = reservationTimeDao.save(request);
        return reservationTimeDao.findById(newReservationTimeId);
    }

    public void deleteReservationTime(Long id) {
        if(reservationDao.existsByTimeId(id)){
            throw new ReservationTimeInUseException("해당 시간에 예약이 존재하여 삭제할 수 없습니다.");
        }
        reservationTimeDao.deleteById(id);
    }
}
