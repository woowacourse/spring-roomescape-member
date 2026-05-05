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

    public List<ReservationTimeResponse> getReservationTimes(Long themeId, LocalDate date) {
        themeDao.selectById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        //TODO: 지난 날짜 검증
        List<Reservation> reservations = reservationDao.selectByThemeIdAndDate(themeId, date); // 테마 아이디와 날짜에 맞는 예약된 객체들 반환
        List<ReservationTime> reservationTimes = reservationTimeDao.select();
        List<ReservationTimeResponse> responses = new ArrayList<>();

        for (Reservation reservation : reservations) {
            for (ReservationTime reservationTime : reservationTimes) {
                if (reservation.getTime().getStartAt() == reservationTime.getStartAt()) {
                    responses.add(ReservationTimeResponse.from(reservationTime, false));
                } else {
                    responses.add(ReservationTimeResponse.from(reservationTime, true));
                }
            }
        }
        return responses;
    }

    public void deleteReservationTime(long reservationTimeId) {
        reservationTimeDao.delete(reservationTimeId);
    }
}
