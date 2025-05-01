package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.entity.ReservationTime;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ReservationExistException;
import roomescape.exception.ReservationTimeExistException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationTimeService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> times = reservationTimeDao.findAll();
        return times.stream()
                .map(ReservationTimeResponse::of)
                .toList();
    }

    public ReservationTimeResponse add(ReservationTimeRequest requestDto) {
        if (reservationTimeDao.isExistByTime(requestDto.startAt())) {
            throw new ReservationTimeExistException("동일한 시간이 이미 존재합니다.");
        }
        ReservationTime reservationTime = new ReservationTime(requestDto.startAt());
        ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);
        return ReservationTimeResponse.of(savedReservationTime);
    }

    public void deleteById(Long id) {
        if (reservationDao.isExistByTimeId(id)) {
            throw new ReservationExistException("이 시간의 예약이 존재합니다.");
        }
        reservationTimeDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 예약시간이 없습니다."));
        reservationTimeDao.deleteById(id);
    }
}
