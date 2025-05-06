package roomescape.application.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.ReservationTimeAvailableResponse;
import roomescape.application.dto.ReservationTimeRequest;
import roomescape.application.dto.ReservationTimeResponse;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;

@Service
public class ReservationTimeService {

    private static final String ERROR_RESERVATION_TIME_WITH_HAS_RESERVATION = "예약 시간에 존재하는 예약 정보가 있습니다.";

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public List<ReservationTimeAvailableResponse> findAvailableTimes(LocalDate date, Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        return reservationTimes.stream()
                .map(time -> {
                    boolean alreadyBooked = reservationDao.existsByDateAndTimeIdAndThemeId(date, time.getId(), themeId);
                    return new ReservationTimeAvailableResponse(time, alreadyBooked);
                }).toList();
    }

    public ReservationTimeResponse createReservationTime(ReservationTimeRequest request) {
        ReservationTime reservationTimeWithoutId = request.toTime();

        ReservationTime savedReservationTime = saveReservationTime(reservationTimeWithoutId);
        return new ReservationTimeResponse(savedReservationTime);
    }

    public void deleteReservationTime(long id) {
        validateNoReservationsForTime(id);
        reservationTimeDao.deleteById(id);
    }

    private ReservationTime saveReservationTime(ReservationTime reservationTimeWithoutId) {
        Long id = reservationTimeDao.save(reservationTimeWithoutId);
        return reservationTimeWithoutId.copyWithId(id);
    }

    private void validateNoReservationsForTime(long id) {
        boolean hasTime = reservationDao.existsByTimeId(id);
        if (hasTime) {
            throw new IllegalArgumentException(ERROR_RESERVATION_TIME_WITH_HAS_RESERVATION);
        }
    }
}
