package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.exception.ReservationTimeException;

@Service
public class ReservationTimeService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationDao reservationDao,
        ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponseDto> getAllReservationTimes() {
        return reservationTimeDao.findAllReservationTimes().stream()
            .map(ReservationTimeResponseDto::from)
            .toList();
    }

    public ReservationTimeResponseDto saveReservationTime(
        ReservationTimeRequestDto reservationTimeRequestDto) {
        ReservationTime reservationTime = reservationTimeRequestDto.toReservationTime();
        reservationTimeDao.saveReservationTime(reservationTime);
        return ReservationTimeResponseDto.from(reservationTime);
    }

    public void deleteReservationTime(Long id) {
        if (reservationDao.findByTimeId(id) != 0) {
            throw new ReservationTimeException("이미 예약된 예약 시간을 삭제할 수 없습니다.");
        }
        reservationTimeDao.deleteReservationTime(id);
    }
}
