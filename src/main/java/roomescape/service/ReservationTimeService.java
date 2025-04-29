package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.persistence.ReservationTimeDao;
import roomescape.service.param.CreateReservationTimeParam;
import roomescape.service.result.ReservationTimeResult;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTImeDao;

    public ReservationTimeService(ReservationTimeDao reservationTImeDao) {
        this.reservationTImeDao = reservationTImeDao;
    }

    public Long create(CreateReservationTimeParam createReservationTimeParam) {
        return reservationTImeDao.create(new ReservationTime(createReservationTimeParam.startAt()));
    }

    public ReservationTimeResult findById(Long reservationTimeId) {
        ReservationTime reservationTime = reservationTImeDao.findById(reservationTimeId).orElseThrow(
                () -> new IllegalArgumentException(reservationTimeId + "에 해당하는 reservation_time 튜플이 없습니다."));
        return toReservationResult(reservationTime);
    }

    public List<ReservationTimeResult> findAll() {
        List<ReservationTime> reservationTimes = reservationTImeDao.findAll();
        return reservationTimes.stream()
                .map(this::toReservationResult)
                .toList();
    }

    public void deleteById(Long reservationTimeId) {
        reservationTImeDao.deleteById(reservationTimeId);
    }

    private ReservationTimeResult toReservationResult(ReservationTime reservationTime) {
        return new ReservationTimeResult(reservationTime.id(), reservationTime.startAt());
    }
}
