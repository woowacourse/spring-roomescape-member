package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.ReservationTimeRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.service.reservation.ReservationTime;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public ReservationTimeResponse createReservationTime(final ReservationTimeRequest reservationTimeRequest) {
        if (reservationTimeDao.existsByTime(reservationTimeRequest.startAt())) {
            throw new IllegalArgumentException("이미 존재하는 시간입니다.");
        }
        final ReservationTime reservationTime = reservationTimeRequest.converToReservationTime();
        final ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);
        return new ReservationTimeResponse(savedReservationTime);
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public void deleteReservationTimeById(final long id) {
        if (reservationDao.existsByTimeId(id)) {
            throw new IllegalArgumentException("예약이 존재하는 시간은 삭제할 수 없습니다.");
        }
        reservationTimeDao.deleteById(id);
    }
}
