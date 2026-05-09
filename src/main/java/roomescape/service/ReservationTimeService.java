package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;

@Service
public class ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public List<ReservationTime> findAll() {
        return reservationTimeDao.findAll();
    }

    public ReservationTime save(LocalTime startAt) {
        if (reservationTimeDao.existsByStartAt(startAt)) {
            throw new IllegalArgumentException("이미 존재하는 예약 시간입니다.");
        }
        return reservationTimeDao.save(startAt);
    }

    public void delete(long id) {
        if (reservationDao.existsByTimeId(id)) {
            throw new IllegalArgumentException("예약에 사용 중인 시간은 삭제할 수 없습니다.");
        }
        reservationTimeDao.delete(id);
    }
}
