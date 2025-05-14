package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.model.User;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final Clock clock;

    public ReservationService(ReservationDao reservationDao,
                              ReservationTimeDao reservationTimeDao,
                              ThemeDao themeDao,
                              Clock clock) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.clock = clock;
    }

    public ReservationResponse addReservation(User user, ReservationRequest reservationRequest) {
        Reservation reservation = reservationOf(user, reservationRequest);
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

    private Reservation reservationOf(User user, ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 시간입니다."));
        Theme theme = themeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 테마입니다."));
        return reservationRequest.toEntityWith(user, reservationTime, theme);
    }

    public void deleteReservation(Long id) {
        boolean isDeleted = reservationDao.deleteById(id);
        if (!isDeleted) {
            throw new IllegalArgumentException("해당하는 id가 없습니다");
        }
    }

    public List<ReservationResponse> getReservations() {
        return reservationDao.findAll()
                .stream()
                .map(ReservationResponse::fromEntity)
                .toList();
    }

    public List<ReservationResponse> getFilteredReservations(Long themeId, Long memberId, LocalDate dateFrom,
                                                             LocalDate dateTo) {
        return reservationDao.findFilteredAll(themeId, memberId, dateFrom, dateTo)
                .stream()
                .map(ReservationResponse::fromEntity)
                .toList();
    }
}
