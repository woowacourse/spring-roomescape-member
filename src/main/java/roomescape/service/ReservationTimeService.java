package roomescape.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.controller.request.ReservationTimeRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.DuplicatedException;
import roomescape.exception.NotFoundException;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationTimeService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationDao reservationDao,
                                  ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeDao.findAllReservationTimes();
    }

    public ReservationTime addReservationTime(ReservationTimeRequest request) {
        LocalTime startAt = request.getStartAt();
        Long countReservationTimeByStartAt = reservationTimeDao.countReservationTimeByStartAt(startAt);
        if (countReservationTimeByStartAt == null || countReservationTimeByStartAt > 0) {
            throw new DuplicatedException("이미 존재하는 시간입니다.");
        }
        ReservationTime reservationTime = new ReservationTime(startAt);
        return reservationTimeDao.addReservationTime(reservationTime);
    }

    public ReservationTime findReservationTime(long id) {
        return reservationTimeDao.findReservationById(id);
    }

    public void deleteReservationTime(long id) {
        Long countedReservationTime = reservationTimeDao.countReservationTimeById(id);
        if (countedReservationTime == null || countedReservationTime <= 0) {
            throw new NotFoundException("id(%s)에 해당하는 예약 시간이 존재하지 않습니다.".formatted(id));
        }
        Long countedReservationByTime = reservationDao.countReservationByTimeId(id);
        if (countedReservationByTime == null || countedReservationByTime > 0) {
            throw new BadRequestException("해당 시간에 예약이 존재하여 삭제할 수 없습니다.");
        }
        reservationTimeDao.deleteReservationTime(id);
    }
}
