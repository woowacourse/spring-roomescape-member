package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.request.ReservationTimeWithBookStatusRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ReservationTimeWithBookStatusResponse;
import roomescape.exception.InvalidInputException;
import roomescape.exception.TargetNotExistException;

@Service
public class ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll()
                .stream()
                .map(ReservationTimeResponse::fromReservationTime)
                .toList();
    }

    public ReservationTimeResponse save(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRequest.toReservationTime();

        validateTimeExistence(reservationTime.getStartAt());

        ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);
        return ReservationTimeResponse.fromReservationTime(savedReservationTime);
    }

    public void deleteById(long id) {
        boolean deleted = reservationTimeDao.deleteById(id);
        if (!deleted) {
            throw new TargetNotExistException("삭제할 예약 시간이 존재하지 않습니다.");
        }
    }

    public List<ReservationTimeWithBookStatusResponse> findReservationTimesWithBookStatus(
            ReservationTimeWithBookStatusRequest timeRequest) {
        return reservationTimeDao.findAllWithBookStatus(timeRequest.date(), timeRequest.themeId());
    }

    private void validateTimeExistence(LocalTime startAt) {
        boolean exists = reservationTimeDao.existByStartAt(startAt);
        if (exists) {
            throw new InvalidInputException("이미 존재하는 예약 시간입니다.");
        }
    }
}
