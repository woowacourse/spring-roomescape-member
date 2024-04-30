package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeAddRequest;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationTimeService {

    private ReservationTimeDao reservationTimeDao;

    ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTime> findAllReservationTime() {
        return reservationTimeDao.findAll();
    }

    public ReservationTime addReservationTime(ReservationTimeAddRequest reservationTimeAddRequest) {
        return reservationTimeDao.insert(reservationTimeAddRequest.toEntity());
    }

    public void removeReservationTime(Long id) {
        if (reservationTimeDao.findById(id).isEmpty()) {
            throw new IllegalArgumentException("해당 id를 가진 예약시간이 존재하지 않습니다.");
        }
        reservationTimeDao.deleteById(id);
    }
}
