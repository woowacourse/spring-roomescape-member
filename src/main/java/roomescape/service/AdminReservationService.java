package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.UserDao;
import roomescape.dto.AdminReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.model.User;

@Service
public class AdminReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final UserDao userDao;
    private final Clock clock;

    public AdminReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,
                                   ThemeDao themeDao,
                                   UserDao userDao, Clock clock) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.userDao = userDao;
        this.clock = clock;
    }

    public ReservationResponse addReservation(AdminReservationRequest reservationRequest) {
        Reservation reservation = reservationOf(reservationRequest);
        if (reservation.isPast(LocalDate.now(clock))) {
            throw new IllegalStateException("하루 전 까지 예약 가능합니다.");
        }
        if (reservationDao.isExistByThemeIdAndTimeIdAndDate(
                reservationRequest.themeId(),
                reservationRequest.timeId(),
                reservationRequest.date())
        ) {
            throw new IllegalStateException("이미 해당 시간에 예약이 존재합니다.");
        }
        Reservation savedReservation = reservationDao.save(reservation);
        return ReservationResponse.fromEntity(savedReservation);
    }

    private Reservation reservationOf(AdminReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 시간입니다."));
        Theme theme = themeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 테마입니다."));
        User user = userDao.findById(reservationRequest.memberId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));
        Reservation reservation = reservationRequest.toEntityWith(user, reservationTime, theme);
        return reservation;
    }
}
