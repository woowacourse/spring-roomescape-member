package roomescape.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.model.ReservationTime;

@Service
public class ReservationTimeService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeResponse addTime(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime time = reservationTimeRequest.toEntity();
        ReservationTime savedTime = reservationTimeDao.save(time);
        return ReservationTimeResponse.fromEntity(savedTime);
    }

    public void deleteTime(Long id) {
        int count = reservationDao.countByTimeId(id);
        if(count != 0) {
            throw new IllegalStateException("예약이 존재하여 삭제할 수 없습니다.");
        }
        boolean isDeleted = reservationTimeDao.deleteById(id);
        if (!isDeleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 id가 없습니다");
        }
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimeResponse::fromEntity)
                .toList();
    }
}
