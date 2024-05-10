package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.dto.reservationtime.ReservationTimeCreateRequest;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.service.exception.InvalidRequestException;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> allReservationTimes = reservationTimeDao.readAll();
        return allReservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse add(ReservationTimeCreateRequest request) {
        ReservationTime reservationTime = request.toDomain();
        validateDuplicateStartAt(reservationTime);
        ReservationTime result = reservationTimeDao.create(reservationTime);
        return ReservationTimeResponse.from(result);
    }

    private void validateDuplicateStartAt(ReservationTime reservationTime) {
        if (reservationTimeDao.exist(reservationTime)) {
            throw new InvalidRequestException("동일한 예약 시간이 존재합니다.");
        }
    }

    public void delete(Long id) {
        validateNull(id);
        validateNotExist(id);
        validateExistReservationByTimeId(id);
        reservationTimeDao.delete(id);
    }

    private void validateNull(Long id) {
        if (id == null) {
            throw new InvalidRequestException("예약 시간 아이디가 없습니다.");
        }
    }

    private void validateNotExist(Long id) {
        if (!reservationTimeDao.exist(id)) {
            throw new InvalidRequestException("해당 아이디를 가진 예약 시간이 존재하지 않습니다.");
        }
    }

    private void validateExistReservationByTimeId(Long id) {
        if (reservationDao.existByTimeId(id)) {
            throw new InvalidRequestException("해당 예약시간을 사용하는 예약이 존재합니다.");
        }
    }
}
