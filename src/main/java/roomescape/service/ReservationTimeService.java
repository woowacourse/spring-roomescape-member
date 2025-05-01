package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.model.ReservationTime;

@Service
public class ReservationTimeService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationTimeService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,
                                  ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public ReservationTimeResponse addTime(ReservationTimeRequest reservationTimeRequest) {
        if (reservationTimeDao.isExistTime(reservationTimeRequest.startAt())) {
            throw new IllegalStateException("이미 존재하는 시간입니다.");
        }
        ReservationTime time = reservationTimeRequest.toEntity();
        ReservationTime savedTime = reservationTimeDao.save(time);
        return ReservationTimeResponse.fromEntity(savedTime);
    }

    public void deleteTime(Long id) {
        if (reservationDao.isExistByTimeId(id)) {
            throw new IllegalStateException("예약이 존재하여 삭제할 수 없습니다.");
        }
        boolean isDeleted = reservationTimeDao.deleteById(id);
        if (!isDeleted) {
            throw new IllegalArgumentException("해당하는 ID가 없습니다.");
        }
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimeResponse::fromEntity)
                .toList();
    }
}
