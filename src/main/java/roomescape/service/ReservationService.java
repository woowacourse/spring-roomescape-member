package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.AlreadyExistException;
import roomescape.common.exception.NotFoundException;
import roomescape.common.exception.UnprocessableException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.request.UpdateReservationRequest;
import roomescape.dto.response.ReservationResponse;

@Service
@Transactional
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final Clock clock;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao,
                              Clock clock) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.clock = clock;
    }

    public ReservationResponse addReservation(ReservationRequest request) {
        ReservationTime reservationTime = getTime(request.timeId());
        Theme theme = getTheme(request.themeId());

        validateUniqueReservation(request.date(), request.timeId(), request.themeId());
        validatePastDatetime(request.date(), reservationTime);

        Reservation reservation = request.toReservation(reservationTime, theme);
        Reservation savedReservation = reservationDao.insert(reservation);
        return ReservationResponse.from(savedReservation);
    }

    private ReservationTime getTime(long timeId) {
        return reservationTimeDao.selectById(timeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시간입니다."));
    }

    private Theme getTheme(long themeId) {
        return themeDao.selectById(themeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));
    }

    private void validateUniqueReservation(LocalDate date, long timeId, long themeId) {
        boolean exists = reservationDao.existsByDateAndTimeIdAndThemeId(date, timeId, themeId);
        if (exists) {
            throw new AlreadyExistException("동일한 날짜, 시간, 테마에 이미 예약이 존재합니다.");
        }
    }

    private void validatePastDatetime(LocalDate date, ReservationTime reservationTime) {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime reservationDateAndTime = LocalDateTime.of(date, reservationTime.getStartAt());

        if (reservationDateAndTime.isBefore(now)) {
            throw new UnprocessableException("지나간 날짜·시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationDao.select();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getMyReservation(String name) {
        List<Reservation> reservations = reservationDao.selectByName(name);
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse update(Long reservationId, UpdateReservationRequest request) {
        Reservation reservation = getReservation(reservationId);
        ReservationTime time = getTime(request.timeId());
        validateUniqueReservation(request.date(), request.timeId(), reservation.getTheme().getId());
        validatePastDatetime(request.date(), time);

        Reservation updateReservation = reservationDao.update(reservationId, request.date(), request.timeId());
        return ReservationResponse.from(updateReservation);
    }

    private Reservation getReservation(Long reservationId) {
        return reservationDao.selectById(reservationId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
    }

    public void delete(Long reservationId) {
        int deleted = reservationDao.delete(reservationId);
        if (deleted == 0) {
            throw new NotFoundException("존재하지 않는 예약입니다.");
        }
    }
}
