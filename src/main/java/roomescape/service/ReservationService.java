package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservation.ReservationTimeInfosResponse;
import roomescape.dto.reservation.ReservationsResponse;
import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.DataConflictException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;

    public ReservationService(final ReservationDao reservationDao,
                              final TimeDao timeDao,
                              final ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
    }

    public ReservationsResponse findAllReservations() {
        List<ReservationResponse> response = reservationDao.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();

        return new ReservationsResponse(response);
    }

    public ReservationTimeInfosResponse findReservationsByDateAndThemeId(final LocalDate date, final Long themeId) {
        return timeDao.findByDateAndThemeId(date, themeId);
    }

    public void removeReservationById(final Long id) {
        reservationDao.deleteById(id);
    }

    public ReservationResponse addReservation(final ReservationRequest reservationRequest) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate requestDate = reservationRequest.date();
        Time requestTime = timeDao.findById(reservationRequest.timeId());
        Theme theme = themeDao.findById(reservationRequest.themeId());

        validateDateAndTime(requestDate, requestTime, now);
        validateReservationDuplicate(reservationRequest, theme);

        Reservation savedReservation = reservationDao.insert(reservationRequest.toReservation(requestTime, theme));

        return ReservationResponse.from(savedReservation);
    }

    private void validateDateAndTime(final LocalDate requestDate, final Time requestTime, final LocalDateTime now) {
        if (isReservationInPast(requestDate, requestTime, now)) {
            throw new DataConflictException(ErrorType.RESERVATION_PERIOD_CONFLICT,
                    String.format("지난 날짜나 시간은 예약이 불가능합니다. [now: %s %s | request: %s %s]",
                            now.toLocalDate(), now.toLocalTime(), requestDate, requestTime.getStartAt()));
        }
    }

    private boolean isReservationInPast(final LocalDate requestDate, final Time requestTime, final LocalDateTime now) {
        LocalDate today = now.toLocalDate();
        LocalTime nowTime = now.toLocalTime();

        // 날짜가 지난 경우
        if (requestDate.isBefore(today)) {
            return true;
        }
        // 날짜가 오늘이지만, 지난 시간인 경우
        if (requestDate.isEqual(today) && requestTime.getStartAt().isBefore(nowTime)) {
            return true;
        }
        return false;
    }

    private void validateReservationDuplicate(final ReservationRequest reservationRequest, final Theme theme) {
        List<Reservation> duplicateTimeReservations = reservationDao.findByTimeIdAndDateAndThemeId(
                reservationRequest.timeId(), reservationRequest.date(), theme.getId());

        if (duplicateTimeReservations.size() > 0) {
            throw new DataConflictException(ErrorType.RESERVATION_DUPLICATION_CONFLICT,
                    String.format("이미 해당 날짜/시간/테마에 예약이 존재합니다. [date: %s | timeId: %d | themeId: %d]",
                            reservationRequest.date(), reservationRequest.timeId(), reservationRequest.themeId()));
        }
    }
}
