package roomescape.service.reservation;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.reservation.ReservationDao;
import roomescape.dao.reservation.ReservationThemeDao;
import roomescape.dao.reservation.ReservationTimeDao;
import roomescape.domain.reservation.ReservationTime;
import roomescape.dto.time.BookableTimeResponse;
import roomescape.dto.time.TimeRequest;
import roomescape.dto.time.TimeResponse;

@Service
public class ReservationTimeService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationThemeDao reservationThemeDao;

    public ReservationTimeService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,
                                  ReservationThemeDao reservationThemeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.reservationThemeDao = reservationThemeDao;
    }

    public List<TimeResponse> getAllTimes() {
        return reservationTimeDao.findAll().stream()
                .map(TimeResponse::new)
                .toList();
    }

    public TimeResponse insertTime(TimeRequest request) {
        ReservationTime time = getTime(request);
        ReservationTime inserted = reservationTimeDao.insert(time);

        return new TimeResponse(inserted);
    }

    private ReservationTime getTime(TimeRequest request) {
        validateDuplicate(request.startAt());

        return new ReservationTime(null, request.startAt());
    }

    private void validateDuplicate(LocalTime time) {
        if (reservationTimeDao.hasSameTime(time)) {
            throw new IllegalArgumentException("동일한 시간이 존재합니다.");
        }
    }

    public List<BookableTimeResponse> getAllBookableTimes(String date, Long themeId) {
        if (!reservationThemeDao.isExist(themeId)) {
            throw new IllegalArgumentException("테마가 존재하지 않아 예약 가능 시간을 조회할 수 없습니다.");
        }
        return reservationTimeDao.getAllBookableTime(date, themeId);
    }

    public void deleteTime(Long id) {
        if (reservationDao.hasReservationForTimeId(id)) {
            throw new IllegalStateException("예약이 존재하는 시간은 삭제할 수 없습니다.");
        }
        reservationTimeDao.deleteById(id);
    }
}
