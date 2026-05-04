package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public List<ReservationTime> findAll(){
        return reservationTimeDao.findAll();
    }

    public ReservationTime save(ReservationTime reservationTime){
        if(reservationTimeDao.existsByStartAt(reservationTime.getStartAt())){
            throw new IllegalArgumentException("이미 존재하는 예약시간입니다.");
        }
        return reservationTimeDao.save(reservationTime);
    }

    public void deleteById(Long id){
        validateHasTime(id);
        reservationTimeDao.deleteById(id);
    }

    private void validateHasTime(Long id) {
        boolean hasReservation = reservationDao.existByTimeId(id);
        if(hasReservation){
            throw new IllegalArgumentException("예약이 존재해 삭제할 수 없습니다");
        }
    }
}
