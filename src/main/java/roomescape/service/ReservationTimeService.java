package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll()
                .stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public ReservationTimeResponse save(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRequest.toReservationTime();

        validateTimeExistence(reservationTime.getStartAt());

        ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);
        return new ReservationTimeResponse(savedReservationTime);
    }

    public boolean deleteById(long id) {
        return reservationTimeDao.deleteById(id);
    }

    private void validateTimeExistence(LocalTime startAt) {
        boolean exists = reservationTimeDao.existByStartAt(startAt);
        if(exists) {
            throw new IllegalArgumentException("이미 존재하는 예약 시간입니다.");
        }
    }
}
