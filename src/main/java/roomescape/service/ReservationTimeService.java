package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.NotFoundException;
import roomescape.common.exception.UnprocessableException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.CreateReservationTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;

@Service
@Transactional
public class ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ThemeDao themeDao,
                                  ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public CreateReservationTimeResponse addReservationTime(ReservationTimeRequest request) {
        ReservationTime reservationTime = request.toReservationTime();
        ReservationTime newReservationTime = reservationTimeDao.insert(reservationTime);
        return CreateReservationTimeResponse.from(newReservationTime);
    }

    @Transactional(readOnly = true)
    public List<ReservationTimeResponse> getReservationTimes(Long themeId, LocalDate date) {
        validateTheme(themeId);

        List<Reservation> reservations = reservationDao.selectByThemeIdAndDate(themeId, date);
        List<ReservationTime> reservationTimes = reservationTimeDao.selectAll();

        return reservationTimes.stream()
                .map(time -> ReservationTimeResponse.from(time, time.isNotReserved(reservations)))
                .toList();
    }

    private void validateTheme(Long themeId) {
        boolean exists = themeDao.existsById(themeId);
        if (!exists) {
            throw new NotFoundException("존재하지 않는 테마입니다.");
        }
    }

    public void deleteReservationTime(long reservationTimeId) {
        Optional<ReservationTime> reservationTime = reservationTimeDao.selectById(reservationTimeId);
        if (reservationTime.isEmpty()) {
            throw new NotFoundException("존재하지 않는 예약 시간입니다.");
        }

        boolean existsByTimeId = reservationDao.existsByTimeId(reservationTimeId);
        if (existsByTimeId) {
            throw new UnprocessableException("예약된 시간은 삭제할 수 없습니다.");
        }
        reservationTimeDao.delete(reservationTimeId);
    }
}
