package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.request.ReservationTimePostRequest;
import roomescape.dto.response.ReservationTimePostResponse;
import roomescape.entity.ReservationTime;

@Service
public class ReservationTimeCommandService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeCommandService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimePostResponse createTime(ReservationTimePostRequest timeRequest) {
        ReservationTime reservationTimeWithoutId = timeRequest.toTime();

        if (reservationTimeDao.existsByStartAt(reservationTimeWithoutId)) {
            throw new IllegalArgumentException("이미 존재하는 시간입니다.");
        }
        ReservationTime reservationTime = reservationTimeDao.create(reservationTimeWithoutId);
        return new ReservationTimePostResponse(reservationTime);
    }

    public void deleteTime(long id) {
        boolean hasReservation = reservationDao.existByTimeId(id);
        if (hasReservation) {
            throw new IllegalArgumentException("해당 시간에 예약한 예약 정보가 있습니다.");
        }
        reservationTimeDao.deleteById(id);
    }
}
