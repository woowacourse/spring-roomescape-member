package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.RoomThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final RoomThemeDao roomThemeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,
                              RoomThemeDao roomThemeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.roomThemeDao = roomThemeDao;
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll()
                .stream()
                .map(ReservationResponse::fromReservation)
                .toList();
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.timeId());
        RoomTheme roomTheme = roomThemeDao.findById(reservationRequest.themeId());
        Reservation reservation = reservationRequest.toReservation(reservationTime, roomTheme);

        validateOutdatedDateTime(reservation.getDate(), reservationTime.getStartAt());
        validateDuplicatedDateTime(reservation.getDate(), reservationTime.getId());

        Reservation savedReservation = reservationDao.save(reservation);
        return ReservationResponse.fromReservation(savedReservation);
    }

    public boolean deleteById(long id) {
        return reservationDao.deleteById(id);
    }

    private void validateOutdatedDateTime(LocalDate date, LocalTime time) {
        LocalDateTime now = LocalDateTime.now(Clock.systemDefaultZone());
        if (LocalDateTime.of(date, time).isBefore(now)) {
            throw new IllegalArgumentException("지난 날짜에는 예약할 수 없습니다.");
        }
    }

    private void validateDuplicatedDateTime(LocalDate date, Long timeId) {
        boolean exists = reservationDao.existsByDateTime(date, timeId);
        if (exists) {
            throw new IllegalArgumentException("예약이 이미 존재합니다.");
        }
    }
}
