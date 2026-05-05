package roomescape.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.CreateReservationTimeResponse;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ThemeDao themeDao, ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public CreateReservationTimeResponse addReservationTime(ReservationTimeRequest request) {
        ReservationTime reservationTime = request.toReservationTime();
        ReservationTime newReservationTime = reservationTimeDao.insert(reservationTime);
        return CreateReservationTimeResponse.from(newReservationTime);
    }

    // TODO: 지난 날짜 검증
    public List<ReservationTimeResponse> getReservationTimes(Long themeId, LocalDate date) {
        themeDao.selectById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        List<Reservation> reservations = reservationDao.selectByThemeIdAndDate(themeId, date);
        List<ReservationTime> reservationTimes = reservationTimeDao.select();

        List<ReservationTimeResponse> responses = new ArrayList<>();
        for (Reservation reservation : reservations) {
            for (ReservationTime reservationTime : reservationTimes) {
                responses.add(createReservationTimeResponse(reservation, reservationTime));
            }
        }
        return responses;
    }

    private ReservationTimeResponse createReservationTimeResponse(Reservation reservation, ReservationTime reservationTime) {
        if (reservation.getTime().getStartAt().equals(reservationTime.getStartAt())) {
            return ReservationTimeResponse.from(reservationTime, false);
        }
        return ReservationTimeResponse.from(reservationTime, true);
    }

    public void deleteReservationTime(long reservationTimeId) {
        reservationTimeDao.delete(reservationTimeId);
    }
}
