package roomescape.service;

import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.entity.ReservationTime;

@Component
public class ReservationTimeCommandService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeCommandService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeResponse createTime(ReservationTimeRequest timeRequest) {
        ReservationTime reservationTimeWithoutId = timeRequest.toTime();
        ReservationTime reservationTime = reservationTimeDao.create(reservationTimeWithoutId);
        return new ReservationTimeResponse(reservationTime);
    }

    public void deleteTime(long id) {
        boolean hasReservation = reservationDao.existByTimeId(id);
        if (hasReservation) {
            throw new IllegalArgumentException("해당 시간에 예약한 예약 정보가 있습니다.");
        }
        reservationTimeDao.deleteById(id);
    }
}
