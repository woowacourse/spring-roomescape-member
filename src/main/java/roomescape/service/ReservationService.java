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
import roomescape.exception.BadRequestException;
import roomescape.service.dto.request.ReservationRequest;
import roomescape.service.dto.response.ReservationResponse;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final RoomThemeDao roomThemeDao;

    public ReservationService(
            ReservationDao reservationDao,
            ReservationTimeDao reservationTimeDao,
            RoomThemeDao roomThemeDao)
    {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.roomThemeDao = roomThemeDao;
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.timeId());
        validateOutdatedDateTime(reservationRequest.date(), reservationTime.getStartAt());

        RoomTheme roomTheme = roomThemeDao.findById(reservationRequest.themeId());
        Reservation reservation = reservationRequest.toReservation(reservationTime, roomTheme);

        validateDuplicatedDateTime(reservation.getDate(), reservationTime.getId(), roomTheme.getId());

        Reservation savedReservation = reservationDao.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    public boolean deleteById(Long id) {
        return reservationDao.deleteById(id);
    }

    private void validateOutdatedDateTime(LocalDate date, LocalTime time) {
        LocalDateTime now = LocalDateTime.now(Clock.systemDefaultZone());
        if (LocalDateTime.of(date, time).isBefore(now)) {
            throw new BadRequestException("지나간 날짜와 시간에 대한 예약을 생성할 수 없습니다.");
        }
    }

    private void validateDuplicatedDateTime(LocalDate date, Long timeId, Long themeId) {
        boolean exists = reservationDao.existsByDateTime(date, timeId, themeId);
        if (exists) {
            throw new BadRequestException("중복된 시간과 날짜에 대한 예약을 생성할 수 없습니다.");
        }
    }
}
